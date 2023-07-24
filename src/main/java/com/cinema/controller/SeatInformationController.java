package com.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cinema.dto.MovieFormDto;
import com.cinema.service.MovieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SeatInformationController {

	private final MovieService movieService;

	@GetMapping(value = "/seatInformation/{movieId}")
	public String movieSelectDate(Model model, @PathVariable("movieId") Long movieId) {
		MovieFormDto movieFormDto = movieService.getMovieDtl(movieId);
		model.addAttribute("movie", movieFormDto);
		return "reservation/seatInformation";
	}
}
