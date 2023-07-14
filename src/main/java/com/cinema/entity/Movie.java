package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.constant.MovieSellStatus;
import com.cinema.dto.MovieFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "movie") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Movie extends BaseEntity {

	@Id
	@Column(name = "mv_id") // 테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.AUTO) // 기본키를 자동으로 생성해주는 전략 사용
	private Long id; // 영화코드

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String movieNm; // 영화명 -> movie_name

	@Column(nullable = false)
	private int price; // 가격 -> price

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String movieYear; // 제작년도 -> movie_year

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String movieDirector; // 감독 -> movie_director

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String movieActors; // 배우들 -> movie_actors

	private LocalDateTime mvPeriodS; // 상영시작일

	private LocalDateTime mvPeriodE; // 상영마감일

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String mvRuntime; // 런타임 -> mv_runtime

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String mvStillcut; // 스틸컷 -> mv_stillcut

	@Column(nullable = false, length = 50) // not null여부, 컬럼 크기지정
	private String mvPosters; // 포스터 -> mv_posters

	private LocalDateTime mvRegdate; // 등록일

	@Lob // clob과 같은 큰타입의 문자타입으로 컬럼을 만든다
	@Column(nullable = false)
	private String movieDetail; // 줄거리 - > mv_summary

	@Column(nullable = false)
	private int stockNumber; // 재고수량 -> stock_number

	@Enumerated(EnumType.STRING) // enum의 이름을 DB의 저장
	private MovieSellStatus movieSellStatus; // 판매상태(SELL, SOLD_OUT) -> item_sell_status

	// movie 엔티티 수정
	public void updateMovie(MovieFormDto movieFormDto) {
		this.movieNm = movieFormDto.getMovieNm();
		this.price = movieFormDto.getPrice();
		this.movieYear = movieFormDto.getMovieYear();
		this.movieDirector = movieFormDto.getMovieDirector();
		this.movieActors = movieFormDto.getMovieActors();
//		this.mvPeriodS = movieFormDto.getMvPeriodS();
//		this.mvPeriodE = movieFormDto.getMvPeriodE();
		this.mvRuntime = movieFormDto.getMvRuntime();
		this.mvStillcut = movieFormDto.getMvStillcut();
		this.mvPosters = movieFormDto.getMvPosters();
//		this.mvRegdate = movieFormDto.getMvRegdate();
		this.movieDetail = movieFormDto.getMovieDetail();

	}

	// 재고 증가
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}

}
