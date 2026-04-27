package com.careconnect.repository;

import com.careconnect.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findByDonorIdOrderByCreatedAtDesc(Long donorId);

  // 🔥 NEW: for logistics
  @EntityGraph(attributePaths = {"assignedTo"})
  List<Item> findByAssignedTo_Id(Long userId);

  @EntityGraph(attributePaths = {"assignedTo"})
  @Override
  List<Item> findAll();

  @EntityGraph(attributePaths = {"assignedTo"})
  @Override
  java.util.Optional<Item> findById(Long id);
}