package com.cinema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.entity.MovieImg;

public interface MovieImgRepository extends JpaRepository<MovieImg, Long> {
	// select * from movie_img where movie_id = ? order by movie_id
	List<MovieImg> findByMovieIdOrderByIdAsc(Long movieId);

	// select * from item_img where item_id = ? and repimg_yn = ?
	MovieImg findByMovieIdAndRepimgYn(Long movieId, String repimgYn);
}
