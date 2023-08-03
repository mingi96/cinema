package com.cinema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinema.entity.MovieImg;
import com.cinema.entity.ReservationMovie;

public interface MovieImgRepository extends JpaRepository<MovieImg, Long> {
	// select * from movie_img where movie_id = ? order by movie_id
	List<MovieImg> findByMovieIdOrderByIdAsc(Long movieId);

	// select * from item_img where item_id = ? and repimg_yn = ?
	MovieImg findByMovieIdAndRepimgYn(Long movieId, String repimgYn);
	

	@Query("select o from MovieImg o where repimgYn = 'Y' and movie.id = :id order by imgUrl asc limit 1")
	MovieImg reserveMovieimg(@Param("id") Long id);
	
}
