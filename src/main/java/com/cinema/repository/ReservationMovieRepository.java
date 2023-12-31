package com.cinema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinema.entity.Reservation;
import com.cinema.entity.ReservationMovie;

public interface ReservationMovieRepository extends JpaRepository<ReservationMovie, Long> {
	// 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
	@Query("select o from ReservationMovie o where o.reservation.member.id = :id")
	List<ReservationMovie> getReservationMovieList(@Param("id") Long id);
	
	@Query("select o from ReservationMovie o where reservation.id = :id")
	ReservationMovie reserveMovieinFo(@Param("id") Long id);
	
	

	

}
