package com.cinema.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cinema.dto.MainMovieDto;
import com.cinema.dto.MovieRankDto;
import com.cinema.dto.MovieSearchDto;
import com.cinema.service.MovieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final MovieService movieService;

	@GetMapping(value = "/")
	public String main(MovieSearchDto movieSearchDto, Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainMovieDto> movies = movieService.getMainMoviePage(movieSearchDto, pageable);

//		List<MovieRankDto> moviesRank = movieService.getMovieRankList();

		model.addAttribute("movies", movies);
//		model.addAttribute("moviesRank", moviesRank);
		model.addAttribute("movieSearchDto", movieSearchDto);

		return "main";
	}
}
