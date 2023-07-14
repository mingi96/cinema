package com.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SelectDateController {

	@GetMapping(value = "/selectDate")
	public String movieInfo() {
		return "reservation/selectDate";
	}
}
