package com.cinema.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cinema.dto.MovieFormDto;
import com.cinema.dto.ReservationHistDto;
import com.cinema.entity.MovieImg;
import com.cinema.entity.ReservationMovie;
import com.cinema.service.MovieService;
import com.cinema.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SelectDateController {
	private final MovieService movieService;

	@GetMapping(value = "/selectDate/{movieId}")
	public String movieSelectDate(Model model, @PathVariable("movieId") Long movieId, Principal principal) {
		MovieFormDto movieFormDto = movieService.getMovieDtl(movieId);
		model.addAttribute("movie", movieFormDto);
		return "reservation/selectDate";
	}
}
