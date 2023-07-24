package com.cinema.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.cinema.constant.MovieSellStatus;
import com.cinema.dto.MainMovieDto;
import com.cinema.dto.MovieSearchDto;
import com.cinema.dto.QMainMovieDto;
import com.cinema.entity.Movie;
import com.cinema.entity.QMovie;
import com.cinema.entity.QMovieImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

	private JPAQueryFactory queryFactory;

	public MovieRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	// 상태를 전체로 했을때 null이 들어있으므로 처리를 한번 해준다
	private BooleanExpression searchSellStatusEq(MovieSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QMovie.movie.movieSellStatus.eq(searchSellStatus);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("movieNm", searchBy)) {
			// 등록자로 검색시
			return QMovie.movie.movieNm.like("%" + searchQuery + "%"); // item_nm like %검색어%
		} else if (StringUtils.equals("createdBy", searchBy)) {
			return QMovie.movie.createdBy.like("%" + searchQuery + "%"); // create_by like %검색어%
		}

		return null; // 쿼리문을 실행하지 않는다.
	}

	@Override
	public Page<Movie> getAdminMoviePage(MovieSearchDto movieSearchDto, Pageable pageable) {
		List<Movie> content = queryFactory.selectFrom(QMovie.movie)
				.where(searchSellStatusEq(movieSearchDto.getSearchSellStatus()),
						searchByLike(movieSearchDto.getSearchBy(), movieSearchDto.getSearchQuery()))
				.orderBy(QMovie.movie.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		long total = queryFactory.select(Wildcard.count).from(QMovie.movie)
				.where(searchSellStatusEq(movieSearchDto.getSearchSellStatus()),
						searchByLike(movieSearchDto.getSearchBy(), movieSearchDto.getSearchQuery()))
				.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression movieNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QMovie.movie.movieNm.like("%" + searchQuery + "%");
	}

	@Override
	public Page<MainMovieDto> getMainMoviePage(MovieSearchDto movieSearchDto, Pageable pageable) {
		QMovie movie = QMovie.movie;
		QMovieImg movieImg = QMovieImg.movieImg;

		// dto 객체로 바로 받아올 때는
		// 1. 컬럼과 dto객체의 필드가 일치해야 한다.
		// 2. dot객체의 생성자에 @QueryProjection를 반드시 사용해야 한다.
		List<MainMovieDto> content = queryFactory
				.select(new QMainMovieDto(movie.id, movie.movieNm, movie.movieDetail, movie.genre, movieImg.imgUrl,
						movie.price))
				.from(movieImg).join(movieImg.movie, movie).where(movieImg.repimgYn.eq("Y"))
				.where(movieNmLike(movieSearchDto.getSearchQuery())).orderBy(movie.id.desc())
				.limit(pageable.getPageSize()).fetch();

		long total = queryFactory.select(Wildcard.count).from(movieImg).join(movieImg.movie, movie)
				.where(movieImg.repimgYn.eq("Y")).where(movieNmLike(movieSearchDto.getSearchQuery())).fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

}
