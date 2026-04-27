package com.careconnect.controller;

import com.careconnect.dto.ItemCreateDto;
import com.careconnect.dto.ItemDto;
import com.careconnect.dto.ItemUpdateDto;
import com.careconnect.entity.User;
import com.careconnect.security.UserPrincipal;
import com.careconnect.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItemController {

  private final ItemService itemService;

  // ================= CREATE =================
  @PostMapping
  public ItemDto create(
      @Valid @RequestBody ItemCreateDto body,
      @AuthenticationPrincipal UserPrincipal principal) {

    User user = principal.getUser();
    return itemService.create(body, user);
  }

  // ================= GET ALL =================
  @GetMapping
  public List<ItemDto> listAll() {
    return itemService.findAll();
  }

  // ================= GET MY =================
  @GetMapping("/my")
  public List<ItemDto> myItems(
      @AuthenticationPrincipal UserPrincipal principal) {

    return itemService.findMine(principal.getUser());
  }

  // 🔥 NEW: LOGISTICS ASSIGNED DONATIONS
  @GetMapping("/logistics")
  public List<ItemDto> getAssignedDonations(
      @AuthenticationPrincipal UserPrincipal principal) {

    return itemService.findAssignedToLogistics(principal.getUser());
  }

  // ================= UPDATE =================
  @PatchMapping("/{id}")
  public ItemDto update(
      @PathVariable Long id,
      @RequestBody ItemUpdateDto body,
      @AuthenticationPrincipal UserPrincipal principal) {

    return itemService.update(id, body, principal.getUser()); 
  }
}