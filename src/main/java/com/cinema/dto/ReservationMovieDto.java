package com.cinema.dto;

import java.util.ArrayList;
import java.util.List;

import com.cinema.entity.ReservationMovie;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationMovieDto {
	// 엔티티 -> Dto로 바꿔준다
	public ReservationMovieDto(ReservationMovie reservationMovie, String imgUrl) {
		this.movieNm = reservationMovie.getMovie().getMovieNm();
		this.count = reservationMovie.getCount();
		this.reservationPrice = reservationMovie.getReservationPrice();
		this.imgUrl = imgUrl;
		this.seat = reservationMovie.getSeat();
	}

	private String movieNm; // 상품명

	private int count; // 주문수량

	private int reservationPrice; // 주문 금액

	private String imgUrl; // 상품 이미지 경로

	private List<String> seat;
}
