package com.careconnect.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

  private Long id;
  private String title;
  private String description;
  private String category;
  private int quantity;
  private String location;
  private Long donorId;
  private String donorName;
  private LocalDateTime createdAt;
  private String status;
  private Long assignedToId;
  private String assignedToName;
  private String donor;
  private LocalDateTime acceptedAt;
  private LocalDateTime deliveredAt;
  private List<ItemRequestSummaryDto> requests;
  private String imageUrl;
}
