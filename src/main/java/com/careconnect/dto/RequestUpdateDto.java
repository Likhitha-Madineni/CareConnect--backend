package com.careconnect.dto;

import lombok.Data;

@Data
public class RequestUpdateDto {

  private String status;

  // ✅ IMPORTANT
  private Long assignedToId;
}