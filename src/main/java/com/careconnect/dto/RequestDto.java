package com.careconnect.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

  private Long id;
  private String status;
  private String message;
  private int quantityRequested;

  private Long itemId;
  private Long requesterId;

  // ✅ IMPORTANT
  private Long assignedToId;

  private ItemSummaryDto donation;
  private ItemSummaryDto item;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}