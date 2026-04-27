package com.careconnect.controller;

import com.careconnect.dto.RequestCreateDto;
import com.careconnect.dto.RequestDto;
import com.careconnect.dto.RequestUpdateDto;
import com.careconnect.security.UserPrincipal;
import com.careconnect.service.CareRequestService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

  private final CareRequestService careRequestService;

  @PostMapping
  public RequestDto create(
      @Valid @RequestBody RequestCreateDto body,
      @AuthenticationPrincipal UserPrincipal principal) {

    return careRequestService.create(body, principal.getUser());
  }

  @GetMapping
  public List<RequestDto> listAll() {
    return careRequestService.findAll();
  }

  @GetMapping("/my")
  public List<RequestDto> myRequests(
      @AuthenticationPrincipal UserPrincipal principal) {

    return careRequestService.findMine(principal.getUser());
  }
  
  @GetMapping("/logistics")
  public List<RequestDto> getAssignedToMe(
      @AuthenticationPrincipal UserPrincipal principal) {

    return careRequestService.findAssignedToLogistics(principal.getUser());
  }

  @PatchMapping("/{id}")
  public RequestDto update(
      @PathVariable Long id,
      @RequestBody RequestUpdateDto body,
      @AuthenticationPrincipal UserPrincipal principal) {

    return careRequestService.update(id, body, principal.getUser());
  }
}