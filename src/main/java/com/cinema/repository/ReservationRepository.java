package com.cinema.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cinema.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	// 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
	@Query("select o from Reservation o where o.member.email = :email order by o.reservationDate desc")
	List<Reservation> findReservations(@Param("email") String email);

	// 현재 로그인한 회원의 주문 개수가 몇개인지 조회
	@Query("select count(o) from Reservation o where o.member.email = :email")
	Long countReservation(@Param("email") String email);
	
	
	@Query("select o from Reservation o where o.id = :id")
	Reservation reserveMovieinFo(@Param("id") Long id);
	
}
