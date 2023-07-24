package com.cinema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.cinema.dto.MovieRankDto;
import com.cinema.entity.Movie;

public interface MovieRepository
		extends JpaRepository<Movie, Long>, QuerydslPredicateExecutor<Movie>, MovieRepositoryCustom {

	// select * from movie where movie_nm = ?
	List<Movie> findByMovieNm(String movieNm);

	// select * from item where item_nm = ? or item_detail = ?
	List<Movie> findByMovieNmOrMovieDetail(String movieNm, String movieDetail);

	// Dto로 바로 넣어주는 native 쿼리를 사용시에 쿼리문에서 별칭을 반드시 줘야 Dto 객체에서 인식한다.
	// 가장 판매순위가 높은 6개의 데이터
	@Query(value = "select data.num num, movie.mv_id id, movie.movie_nm movieNm, movie_img.img_url imgUrl, movie_img.repimg_yn repimgYn \r\n"
			+ "            from movie \r\n" + "			inner join movie_img on (movie.mv_id = movie_img.movie_id)\r\n"
			+ "			inner join (select @ROWNUM\\:=@ROWNUM+1 num, groupdata.* from\r\n"
			+ "			            (select reservation_movie.movie_id, count(*) cnt\r\n"
			+ "			              from reservation_movie\r\n"
			+ "			              inner join reservations on (reservation_movie.reservation_id = reservations.reservation_id)\r\n"
			+ "			              where reservations.reservation_status = 'RESERVATION'\r\n"
			+ "			             group by reservation_movie.movie_id order by cnt desc) groupdata,\r\n"
			+ "                          (SELECT @ROWNUM\\:=0) R \r\n" + "                          limit 6) data\r\n"
			+ "			on (movie.mv_id = data.movie_id)\r\n" + "			where movie_img.repimg_yn = 'Y'\r\n"
			+ "			order by num", nativeQuery = true)
	List<MovieRankDto> getMovieRankList();
}
