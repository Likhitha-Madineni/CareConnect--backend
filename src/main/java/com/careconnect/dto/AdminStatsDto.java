package com.careconnect.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {

  private long totalItems;
  private long pending;
  private long approved;
  private long assigned;
  private long acceptedByLogistics;
  private long delivered;
  private long rejected;
  private long cancelled;
  private long totalRequests;
  private List<UserDto> logisticsUsers;
}
