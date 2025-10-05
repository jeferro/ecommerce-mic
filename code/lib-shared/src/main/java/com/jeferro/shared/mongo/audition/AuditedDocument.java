package com.jeferro.shared.mongo.audition;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@RequiredArgsConstructor
public abstract class AuditedDocument {

  @CreatedDate
  private final Instant createdAt;

  @CreatedBy
  private final String createdBy;

  @LastModifiedDate
  private final Instant updatedAt;

  @LastModifiedBy
  private final String updatedBy;
}
