package com.cinema.entity;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import lombok.*;

@EntityListeners(value = { AuditingEntityListener.class })
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {
	@CreatedBy
	@Column(updatable = false)
	private String createdBy; // 등록자

	@LastModifiedBy
	private String modifiedBy; // 수정자
}
