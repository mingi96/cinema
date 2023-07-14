package com.cinema.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.cinema.constant.ReservationStatus;
import com.cinema.entity.Reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationHistDto {
	// entity -> Dto로 변환
	public ReservationHistDto(Reservation reservation) {
		this.reservationId = reservation.getId();
		this.reservationDate = reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
		this.reservationStatus = reservation.getReservationStatus();
	}

	private Long reservationId; // 주문아이디

	private String reservationDate; // 주문날짜

	private ReservationStatus reservationStatus; // 주문상태

	private List<ReservationMovieDto> reservationMovieDtoList = new ArrayList<>(); // 주문 상품 리스트

	// orderItemDto객체를 주문 상품 리스트에 추가하는 메소드
	public void addReservationMovieDto(ReservationMovieDto reservationMovieDto) {
		reservationMovieDtoList.add(reservationMovieDto);
	}
}
