package com.cinema.dto;

import org.modelmapper.ModelMapper;

import com.cinema.entity.MovieImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieImgDto {
	private Long id;

	private String imgName;

	private String oriImgName;

	private String imgUrl;

	private String repimgYn;

	private static ModelMapper modelMapper = new ModelMapper();

	// entity -> dto로 변환
	public static MovieImgDto of(MovieImg movieImg) {
		return modelMapper.map(movieImg, MovieImgDto.class);
	}
}
