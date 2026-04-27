package com.careconnect.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestCreateDto {

  @NotNull
  private Long itemId;

  @Min(1)
  private int quantityRequested;

  private String message;
}
