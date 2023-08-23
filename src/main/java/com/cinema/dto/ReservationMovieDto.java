package com.cinema.dto;

import java.util.List;

import com.cinema.entity.ReservationMovie;

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
	}

	private String movieNm; // 영화명

	private int count; // 예매수량

	private int reservationPrice; // 예매 금액

	private String imgUrl; // 영화 이미지 경로

	private List<String> seat;

}
