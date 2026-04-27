package com.careconnect.controller;

import com.careconnect.dto.AdminStatsDto;
import com.careconnect.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/stats")
  @PreAuthorize("hasRole('ADMIN')")
  public AdminStatsDto stats() {
    return adminService.getStats();
  }
}
