package com.cinema.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.cinema.constant.MovieSellStatus;
import com.cinema.entity.Movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieFormDto {
	private Long id;

	@NotBlank(message = "영화명은 필수 입력입니다.")
	private String movieNm;
	
	private int price;

	@NotBlank(message = "영화감독은 필수 입력입니다.")
	private String movieDirector;

	@NotBlank(message = "배우들은 필수 입력입니다.")
	private String movieActors;

	@NotBlank(message = "장르는 필수 입력입니다.")
	private String genre;

	@NotBlank(message = "상영시간은 필수 입력입니다.")
	private String mvRuntime;

	private LocalDate mvPeriodS;

	@NotBlank(message = "줄거리는 필수 입력입니다.")
	private String movieDetail;

	private LocalDateTime mvRegdate;

	private MovieSellStatus movieSellStatus;
	
	private List<ReservationDto> seat;
	
	// 영화 이미지 정보를 저장
	private List<MovieImgDto> movieImgDtoList = new ArrayList<>();

	// 영화 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도.
	private List<Long> movieImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	// dto -> entity로 바꿈
	public Movie createMovie() {
		return modelMapper.map(this, Movie.class);
	}

	// entity -> dto로 바꿈
	public static MovieFormDto of(Movie movie) {
		return modelMapper.map(movie, MovieFormDto.class);
	}
}
