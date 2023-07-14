package com.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

	@GetMapping(value = "/info")
	public String movieInfo() {
		return "info/movieInfo";
	}
}
