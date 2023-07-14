package com.cinema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cinema.dto.MainMovieDto;
import com.cinema.dto.MovieSearchDto;
import com.cinema.entity.Movie;

public interface MovieRepositoryCustom {
	Page<Movie> getAdminMoviePage(MovieSearchDto movieSearchDto, Pageable pageable);

	Page<MainMovieDto> getMainMoviePage(MovieSearchDto movieSearchDto, Pageable pageable);
}
