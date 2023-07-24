package com.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TheaterController {

	// 영화관 선택
	@GetMapping(value = "/theater")
	public String theater() {
		return "theater";
	}
}
