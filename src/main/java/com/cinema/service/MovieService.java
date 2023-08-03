package com.cinema.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cinema.dto.MainMovieDto;
import com.cinema.dto.MovieFormDto;
import com.cinema.dto.MovieImgDto;
import com.cinema.dto.MovieRankDto;
import com.cinema.dto.MovieSearchDto;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieImg;
import com.cinema.entity.Reservation;
import com.cinema.repository.MovieImgRepository;
import com.cinema.repository.MovieRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

	private final MovieRepository movieRepository;
	private final MovieImgService movieImgService;
	private final MovieImgRepository movieImgRepository;

	// movie 테이블에 상품등록(insert)
	public Long saveMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {

		// 1.상품등록
		Movie movie = movieFormDto.createMovie(); // dto -> entity
		movieRepository.save(movie); // insert(저장)

		// 2.이미지등록
		for (int i = 0; i < movieImgFileList.size(); i++) {
			// ★fk키를 사용시 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			MovieImg movieImg = new MovieImg();
			movieImg.setMovie(movie);

			// 첫번째 이미지 일때 대표상품 이미지 지정
			if (i == 0) {
				movieImg.setRepimgYn("Y");
			} else {
				movieImg.setRepimgYn("N");
			}

			movieImgService.saveMovieImg(movieImg, movieImgFileList.get(i));
		}

		return movie.getId(); // 등록한 상품 id를 리턴
	}

	// 상품 가져오기
	@Transactional(readOnly = true) // 트랙잰션 읽기 전용(변경감지 수해하지 않음) ->성능 향상
	public MovieFormDto getMovieDtl(Long movieId) {
		// 1.movie_img 테이블의 이미지를 가져온다.
		List<MovieImg> movieImgList = movieImgRepository.findByMovieIdOrderByIdAsc(movieId);

		// MovieImg 엔티티 객체 -> MovieImgDto로 변환
		List<MovieImgDto> movieImgDtoList = new ArrayList<>();
		for (MovieImg movieImg : movieImgList) {
			MovieImgDto movieImgDto = MovieImgDto.of(movieImg);
			movieImgDtoList.add(movieImgDto);
		}

		// 2.movie테이블에 있는데이터를 가져온다.
		Movie movie = movieRepository.findById(movieId).orElseThrow(EntityNotFoundException::new);

		// Movie 엔티티 객체 -> dto로 변환
		MovieFormDto movieFormDto = MovieFormDto.of(movie);

		// 3.MovieFormDto에 이미지 정보(movieImgDtoList)를 넣어준다.
		movieFormDto.setMovieImgDtoList(movieImgDtoList);

		return movieFormDto;

	}

	// 이미지 업데이트 메소드
	public Long UpdateMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {

		// 1. movie 엔티티 가져와서 바꾼다.
		Movie movie = movieRepository.findById(movieFormDto.getId()).orElseThrow(EntityNotFoundException::new);

		movie.updateMovie(movieFormDto);

		// 2. movie_img를 바꿔준다.
		List<Long> movieImgIds = movieFormDto.getMovieImgIds(); // 상품 이미지 아이디 리스트 조회

		for (int i = 0; i < movieImgFileList.size(); i++) {
			movieImgService.updateMovieImg(movieImgIds.get(i), movieImgFileList.get(i));
		}

		return movie.getId(); // 변경한 movie의 id 리턴
	}

	@Transactional(readOnly = true)
	public Page<Movie> getAdminMoviePage(MovieSearchDto movieSearchDto, Pageable pageable) {
		Page<Movie> moviePage = movieRepository.getAdminMoviePage(movieSearchDto, pageable);
		return moviePage;
	}

	@Transactional(readOnly = true)
	public Page<MainMovieDto> getMainMoviePage(MovieSearchDto movieSearchDto, Pageable pageable) {
		Page<MainMovieDto> mainMoviePage = movieRepository.getMainMoviePage(movieSearchDto, pageable);
		return mainMoviePage;

	}

	@Transactional(readOnly = true)
	public List<MovieRankDto> getMovieRankList() {
		return movieRepository.getMovieRankList();
	}
	
	public MovieImg getdeImg(Long id) {
		return movieImgRepository.reserveMovieimg(id);
	}
	
	
	

	
	
}
