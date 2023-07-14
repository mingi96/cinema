package com.cinema.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "theater")
@Getter
@Setter
@ToString
public class Theater {
	@Id
	@Column(name = "si_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
}
