package com.careconnect.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCreateDto {

  @NotBlank
  private String title;

  private String description;

  @NotBlank
  private String category;

  @NotNull
  @Min(1)
  private Integer quantity;

  @NotBlank
  private String location;
  
  private String imageUrl;
}
