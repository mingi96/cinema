package com.cinema.dto;

import com.cinema.constant.MovieSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieSearchDto {
	private String searchDateType;
	private MovieSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery = "";
}
