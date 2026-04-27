package com.careconnect.service;

import com.careconnect.dto.*;
import com.careconnect.entity.*;
import com.careconnect.repository.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CareRequestService {

  private final DonationRequestRepository donationRequestRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final EmailService emailService;

  // ================= CREATE =================
  @Transactional
  public RequestDto create(RequestCreateDto dto, User user) {

    Item item = itemRepository.findById(dto.getItemId())
        .orElseThrow(() -> new IllegalArgumentException("Item not found"));

    DonationRequest r = new DonationRequest();
    r.setUser(user);
    r.setItem(item);
    r.setQuantityRequested(dto.getQuantityRequested());
    r.setMessage(dto.getMessage());
    r.setStatus("PENDING");

    return toDto(donationRequestRepository.save(r));
  }

  // ================= ADMIN =================
  public List<RequestDto> findAll() {
    return donationRequestRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  // ================= USER =================
  public List<RequestDto> findMine(User user) {
    return donationRequestRepository
        .findByUser_IdOrderByCreatedAtDesc(user.getId())
        .stream()
        .map(this::toDto)
        .toList();
  }

  // ================= LOGISTICS =================
  // ✅ IMPORTANT: this is correct and REQUIRED
  public List<RequestDto> findAssignedToLogistics(User user) {
    return donationRequestRepository
        .findByAssignedTo_Id(user.getId())
        .stream()
        .map(this::toDto)
        .toList();
  }

  // ================= UPDATE =================
  @Transactional
  public RequestDto update(Long id, RequestUpdateDto dto, User user) {

    DonationRequest r = donationRequestRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Request not found"));

    String status = dto.getStatus();

    switch (status) {

      case "APPROVED":
        r.setStatus("APPROVED");

        sendMail(
            r.getUser().getEmail(),
            "[CareConnect] Request Approved",
            "Dear " + r.getUser().getName() + ",\n\n" +
            "Your request for '" + r.getItem().getTitle() + "' has been approved.\n\n" +
            "Best regards,\nCareConnect Team"
        );
        break;

      case "REJECTED":
        r.setStatus("REJECTED");

        sendMail(
            r.getUser().getEmail(),
            "[CareConnect] Request Rejected",
            "Dear " + r.getUser().getName() + ",\n\n" +
            "Your request for '" + r.getItem().getTitle() + "' was rejected.\n\n" +
            "Best regards,\nCareConnect Team"
        );
        break;

      case "ASSIGNED":

        if (dto.getAssignedToId() == null) {
          throw new IllegalArgumentException("assignedToId is required");
        }

        User logistics = userRepository.findById(dto.getAssignedToId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        r.setAssignedTo(logistics);
        r.setStatus("ASSIGNED");

        sendMail(
            logistics.getEmail(),
            "[CareConnect] New Delivery Assigned",
            "Dear " + logistics.getName() + ",\n\n" +
            "You have been assigned a delivery for '" + r.getItem().getTitle() + "'.\n\n" +
            "Best regards,\nCareConnect Team"
        );

        sendMail(
            r.getUser().getEmail(),
            "[CareConnect] Request Assigned",
            "Dear " + r.getUser().getName() + ",\n\n" +
            "Your request has been assigned to a logistics partner.\n\n" +
            "Best regards,\nCareConnect Team"
        );

        break;

      case "ACCEPTED":
        r.setStatus("ACCEPTED");
        break;

      case "DELIVERED":
        r.setStatus("DELIVERED");

        sendMail(
            r.getUser().getEmail(),
            "[CareConnect] Delivery Completed",
            "Dear " + r.getUser().getName() + ",\n\n" +
            "Your request has been successfully delivered.\n\n" +
            "Best regards,\nCareConnect Team"
        );
        break;

      case "CANCELLED":
        r.setStatus("CANCELLED");
        break;

      default:
        throw new IllegalArgumentException("Invalid status");
    }

    return toDto(donationRequestRepository.save(r));
  }

  // ================= EMAIL HELPER =================
  private void sendMail(String to, String subject, String body) {
    try {
      emailService.sendEmail(to, subject, body);
    } catch (Exception e) {
      System.out.println("Email failed: " + e.getMessage());
    }
  }

  // ================= DTO =================
  private RequestDto toDto(DonationRequest r) {

    Item item = r.getItem();

    ItemSummaryDto summary = ItemSummaryDto.builder()
        .id(item.getId())
        .title(item.getTitle())
        .category(item.getCategory())
        .build();

    return RequestDto.builder()
        .id(r.getId())
        .status(r.getStatus())
        .message(r.getMessage())
        .quantityRequested(r.getQuantityRequested())
        .itemId(item.getId())
        .requesterId(r.getUser().getId())
        .assignedToId(r.getAssignedTo() != null ? r.getAssignedTo().getId() : null)
        .donation(summary)
        .item(summary)
        .createdAt(r.getCreatedAt())
        .updatedAt(r.getUpdatedAt())
        .build();
  }
}