package com.careconnect.service;

import com.careconnect.dto.AdminStatsDto;
import com.careconnect.dto.UserDto;
import com.careconnect.entity.Item;
import com.careconnect.entity.Role;
import com.careconnect.repository.DonationRequestRepository;
import com.careconnect.repository.ItemRepository;
import com.careconnect.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final ItemRepository itemRepository;
  private final DonationRequestRepository donationRequestRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public AdminStatsDto getStats() {
    List<Item> items = itemRepository.findAll();
    long pending = items.stream().filter(i -> "Pending".equals(i.getStatus())).count();
    long approved = items.stream().filter(i -> "Approved".equals(i.getStatus())).count();
    long assigned = items.stream().filter(i -> "Assigned".equals(i.getStatus())).count();
    long acceptedByLogistics =
        items.stream().filter(i -> "Accepted by Logistics".equals(i.getStatus())).count();
    long delivered = items.stream().filter(i -> "Delivered".equals(i.getStatus())).count();
    long rejected = items.stream().filter(i -> "Rejected".equals(i.getStatus())).count();
    long cancelled =
        items.stream()
            .filter(i -> i.getStatus() != null && i.getStatus().toLowerCase().contains("cancelled"))
            .count();

    List<UserDto> logisticsUsers =
        userRepository.findByRole(Role.LOGISTICS).stream()
            .map(u -> modelMapper.map(u, UserDto.class))
            .toList();

    return AdminStatsDto.builder()
        .totalItems(items.size())
        .pending(pending)
        .approved(approved)
        .assigned(assigned)
        .acceptedByLogistics(acceptedByLogistics)
        .delivered(delivered)
        .rejected(rejected)
        .cancelled(cancelled)
        .totalRequests(donationRequestRepository.count())
        .logisticsUsers(logisticsUsers)
        .build();
  }
}
