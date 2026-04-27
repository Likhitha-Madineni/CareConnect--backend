package com.careconnect.repository;

import com.careconnect.entity.DonationRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {

  @EntityGraph(attributePaths = {"item", "user"})
  List<DonationRequest> findByUser_IdOrderByCreatedAtDesc(Long userId);

  @EntityGraph(attributePaths = {"user"})
  List<DonationRequest> findByItemId(Long itemId);

  @EntityGraph(attributePaths = {"item", "user"})
  List<DonationRequest> findByStatusOrderByCreatedAtDesc(String status);
  
  @EntityGraph(attributePaths = {"item", "user"})
  List<DonationRequest> findByAssignedTo_Id(Long userId);
}
