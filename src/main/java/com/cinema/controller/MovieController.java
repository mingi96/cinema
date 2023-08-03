package com.cinema.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cinema.dto.MovieFormDto;
import com.cinema.dto.MovieSearchDto;
import com.cinema.entity.Movie;
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
		System.out.println(movieFormDto.getMovieImgDtoList().get(1).getImgUrl());
		
		model.addAttribute("movie", movieFormDto);
		return "movie/movieDtl";
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
	@GetMapping(value = { "/admin/movies", "/admin/movies/{page}" })
	public String movieManage(MovieSearchDto movieSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {
		// url 경로에 페이지가 있으면 해당 페이지 번호를 조회하도록 하고 페이지 번호가 없으면 0페이지 조회
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // of(조회 할 페이지의 번호, 한페이지당 조회할 데이터 갯수)

		Page<Movie> movies = movieService.getAdminMoviePage(movieSearchDto, pageable);
		model.addAttribute("movies", movies);
		model.addAttribute("movieSearchDto", movieSearchDto);
		model.addAttribute("maxPage", 5); // 상품관리 페이지 하단에 보여줄 최대 페이지번호

		return "/movie/movieMng";
	}

	// 상품 수정페이지 보기
	@GetMapping(value = "/admin/movie/{movieId}")
	public String movieDtl(@PathVariable("movieId") Long movieId, Model model) {

		try {
			MovieFormDto movieFormDto = movieService.getMovieDtl(movieId);
			model.addAttribute("movieFormDto", movieFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품정보를 가져올때 에러가 발생했브니다.");
			model.addAttribute("movieFormDto", new MovieFormDto());
			return "movie/movieForm";
		}

		return "movie/movieModifyForm";
	}
	
	// 상품 수정(update)
		@PostMapping(value = "/admin/movie/{movieId}")
		public String movieUpdate(@Valid MovieFormDto movieFormDto, Model model, BindingResult bindingResult,
				@RequestParam("movieImgFile") List<MultipartFile> movieImgFileList) {

			System.out.println(movieFormDto.getId() + "ttttttttttttt");
			if (bindingResult.hasErrors()) {
				return "movie/movieForm";
			}

			// 첫번째 이미지가 있는지 검사
			if (movieImgFileList.get(0).isEmpty() && movieFormDto.getId() == null) {
				model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
				return "movie/movieForm";
			}

			try {
				movieService.UpdateMovie(movieFormDto, movieImgFileList);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "상품수정 중 에러가 발생했습니다");
				return "movie/movieForm";
			}

			return "redirect:/";
		}
}
