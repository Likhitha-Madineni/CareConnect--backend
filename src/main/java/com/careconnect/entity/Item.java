package com.careconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(length = 4000)
  private String description;

  private String category;

  private int quantity;

  private String location;

  @Column(nullable = false)
  private Long donorId;

  @Column(nullable = false)
  private String donorName;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  /** Workflow: Pending, Approved, Rejected, Assigned, Accepted by Logistics, etc. */
  @Column(nullable = false)
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assigned_to_id")
  private User assignedTo;

  private LocalDateTime acceptedAt;

  private LocalDateTime deliveredAt;
  
  private String imageUrl;
}
