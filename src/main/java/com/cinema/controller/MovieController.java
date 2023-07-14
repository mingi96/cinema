package com.cinema.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cinema.dto.MovieFormDto;
import com.cinema.service.MovieService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	// 영화전체 리스트
	@GetMapping(value = "/movie/list")
	public String main() {
		return "movie/movieList";
	}

	// 상품 상세 페이지
	@GetMapping(value = "/movie/{movieId}")
	public String movieDtl(Model model, @PathVariable("movieId") Long movieId) {
		MovieFormDto movieFormDto = movieService.getMovieDtl(movieId);
		model.addAttribute("movie", movieFormDto);
		return "movie/movieInfo";
	}

	// 영화등록 페이지
	@GetMapping(value = "/admin/movie/new")
	public String movieForm(Model model) {
		model.addAttribute("movieFormDto", new MovieFormDto());
		return "movie/movieForm";
	}

	// 상품 등록( insert)
	@PostMapping(value = "/admin/movie/new")
	public String movieNew(@Valid MovieFormDto movieFormDto, BindingResult bindingResult, Model model,
			@RequestParam("movieImgFile") List<MultipartFile> movieImgFileList) {

		if (bindingResult.hasErrors()) {
			return "movie/movieForm";
		}

		// 상품등록전에 첫번째 이미지가 있는지 없는지 검사(첫번쨰 이미지는 필수값)
		if (movieImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "movie/movieForm";
		}

		try {
			movieService.saveMovie(movieFormDto, movieImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품등록중 에러가 발생했습니다.");
			return "movie/movieForm";
		}

		return "redirect:/";

	}

	// 영화관리 페이지
	@GetMapping(value = "/admin/items")
	public String movieManage() {
		return "/movie/movieMng";
	}
}
