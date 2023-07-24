package com.cinema.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainMovieDto {
	private Long id;

	private String movieNm;

	private String movieDetail;

	private String genre;

	private String imgUrl;

	private Integer price;

	@QueryProjection
	public MainMovieDto(Long id, String movieNm, String movieDetail, String genre, String imgUrl, Integer price) {
		this.id = id;
		this.movieNm = movieNm;
		this.movieDetail = movieDetail;
		this.genre = genre;
		this.imgUrl = imgUrl;
		this.price = price;
	}
}
