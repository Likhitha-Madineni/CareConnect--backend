package com.careconnect.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {

  private String status;
  private Long assignedToId;
  private String acceptedAt;
  private String deliveredAt;
}
