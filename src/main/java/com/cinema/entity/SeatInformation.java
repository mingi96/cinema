package com.cinema.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Table(name = "seatinformation")
@Getter
@ToString
public class SeatInformation {
	@Id
	@Column(name = "seat_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true, length = 255) // 중복된 값이 들어올 수 없다.
	private String siSeatX;

	@Column(nullable = false, length = 255)
	private String siSeatY;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tt_id")
	private MovieTimeTable movietimetable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ss_id")
	private Theater theater;

}
