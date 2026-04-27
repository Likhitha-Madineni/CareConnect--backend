package com.careconnect.service;

import com.careconnect.dto.ItemCreateDto;
import com.careconnect.dto.ItemDto;
import com.careconnect.dto.ItemUpdateDto;
import com.careconnect.entity.Item;
import com.careconnect.entity.Role;
import com.careconnect.entity.User;
import com.careconnect.repository.ItemRepository;
import com.careconnect.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final EmailService emailService;

  @Transactional
  public ItemDto create(ItemCreateDto dto, User user) {
    Item item = new Item();

    item.setTitle(dto.getTitle());
    item.setDescription(dto.getDescription());
    item.setCategory(dto.getCategory());
    item.setQuantity(dto.getQuantity());
    item.setLocation(dto.getLocation());
    item.setImageUrl(dto.getImageUrl());

    item.setDonorId(user.getId());
    item.setDonorName(user.getName());

    item.setCreatedAt(LocalDateTime.now());
    item.setStatus("PENDING");

    return toDto(itemRepository.save(item));
  }

  @Transactional(readOnly = true)
  public List<ItemDto> findAll() {
    return itemRepository.findAll().stream()
        .filter(item -> !"ASSIGNED".equals(item.getStatus()))
        .map(this::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ItemDto> findMine(User user) {
    return itemRepository.findByDonorIdOrderByCreatedAtDesc(user.getId())
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Transactional
  public ItemDto update(Long id, ItemUpdateDto dto, User user) {

    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Item not found"));

    String status = dto.getStatus();

    User donor = userRepository.findById(item.getDonorId())
        .orElseThrow(() -> new IllegalArgumentException("Donor not found"));

    if (user.getRole() == Role.ADMIN) {

      if ("APPROVED".equals(status)) {
        item.setStatus("APPROVED");

        emailService.sendEmail(
            donor.getEmail(),
            "[CareConnect] Donation Approved",
            "Dear " + donor.getName() + ",\n\n" +
            "We are pleased to inform you that your donation for '" + item.getTitle() + "' has been approved.\n\n" +
            "Our team will soon arrange logistics for pickup and delivery.\n\n" +
            "Thank you for your contribution.\n\n" +
            "Best regards,\nCareConnect Team"
        );
      }

      else if ("REJECTED".equals(status)) {
        item.setStatus("REJECTED");

        emailService.sendEmail(
            donor.getEmail(),
            "[CareConnect] Donation Update",
            "Dear " + donor.getName() + ",\n\n" +
            "We regret to inform you that your donation for '" + item.getTitle() + "' could not be approved.\n\n" +
            "Please review the submission guidelines and try again if applicable.\n\n" +
            "Thank you for your understanding.\n\n" +
            "Best regards,\nCareConnect Team"
        );
      }

      else if ("ASSIGNED".equals(status)) {

        if (dto.getAssignedToId() == null) {
          throw new IllegalArgumentException("assignedToId required");
        }

        User logistics = userRepository.findById(dto.getAssignedToId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        item.setAssignedTo(logistics);
        item.setStatus("ASSIGNED");

        // Mail to logistics
        emailService.sendEmail(
            logistics.getEmail(),
            "[CareConnect] New Delivery Assigned",
            "Dear " + logistics.getName() + ",\n\n" +
            "You have been assigned a delivery for the item '" + item.getTitle() + "'.\n\n" +
            "Please review the details and proceed accordingly.\n\n" +
            "Best regards,\nCareConnect Team"
        );

        // Mail to donor
        emailService.sendEmail(
            donor.getEmail(),
            "[CareConnect] Donation Assigned",
            "Dear " + donor.getName() + ",\n\n" +
            "Your donation for '" + item.getTitle() + "' has been assigned to a logistics partner.\n\n" +
            "They will handle pickup and delivery shortly.\n\n" +
            "Thank you for your generosity.\n\n" +
            "Best regards,\nCareConnect Team"
        );
      }
    }

    else if (user.getRole() == Role.LOGISTICS) {

      if (!item.getAssignedTo().getId().equals(user.getId())) {
        throw new IllegalArgumentException("Not assigned to you");
      }

      if ("ACCEPTED".equals(status)) {
        item.setStatus("ACCEPTED");
        item.setAcceptedAt(LocalDateTime.now());
      }

      else if ("DELIVERED".equals(status)) {
        item.setStatus("DELIVERED");
        item.setDeliveredAt(LocalDateTime.now());

        emailService.sendEmail(
            donor.getEmail(),
            "[CareConnect] Delivery Completed",
            "Dear " + donor.getName() + ",\n\n" +
            "We are happy to inform you that your donated item '" + item.getTitle() + "' has been successfully delivered.\n\n" +
            "Your contribution has made a meaningful impact.\n\n" +
            "Thank you for being a part of CareConnect.\n\n" +
            "Best regards,\nCareConnect Team"
        );
      }
    }

    else {
      if ("CANCELLED".equals(status)) {
        item.setStatus("CANCELLED");
      }
    }

    return toDto(itemRepository.save(item));
  }

  private ItemDto toDto(Item item) {
    return ItemDto.builder()
        .id(item.getId())
        .title(item.getTitle())
        .category(item.getCategory())
        .quantity(item.getQuantity())
        .status(item.getStatus())
        .assignedToId(
            item.getAssignedTo() != null ? item.getAssignedTo().getId() : null
        )
        .imageUrl(item.getImageUrl())
        .build();
  }
  
  @Transactional(readOnly = true)
  public List<ItemDto> findAssignedToLogistics(User user) {
	  return itemRepository.findByAssignedTo_Id(user.getId())
		        .stream()
		        .map(this::toDto)
		        .toList();
  }
}