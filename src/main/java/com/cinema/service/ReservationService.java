package com.cinema.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.cinema.dto.ReservationDto;
import com.cinema.dto.ReservationHistDto;
import com.cinema.dto.ReservationMovieDto;
import com.cinema.entity.Member;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieImg;
import com.cinema.entity.Reservation;
import com.cinema.entity.ReservationMovie;
import com.cinema.repository.MemberRepository;
import com.cinema.repository.MovieImgRepository;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.ReservationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
	private final MovieRepository movieRepository;
	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	private final MovieImgRepository movieImgRepository;

	// 주문하기
	public Long reservation(ReservationDto reservationDto, String email) {

		// 1. 주문할 상품을 조회
		Movie movie = movieRepository.findById(reservationDto.getMovieId()).orElseThrow(EntityNotFoundException::new);

		// 2. 현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);

		// 3. 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
		List<ReservationMovie> reservationMovieList = new ArrayList<>();
		ReservationMovie reservationMovie = ReservationMovie.createReservationMovie(movie, reservationDto.getCount());
		reservationMovieList.add(reservationMovie);

		// 4. 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
		Reservation reservation = Reservation.createReservation(member, reservationMovieList);
		reservationRepository.save(reservation); // insert

		return reservation.getId();
	}

	// 주문 목록을 가져오는 서비스
	@Transactional(readOnly = true)
	public Page<ReservationHistDto> getReservationList(String email, Pageable pageable) {
		// 1. 유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회
		List<Reservation> reservations = reservationRepository.findReservations(email, pageable);

		// 2. 유저의 주문 총 개수를 구한다
		Long totalCount = reservationRepository.countReservation(email);

		List<ReservationHistDto> reservationHistDtos = new ArrayList<>();

		// 3. 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO(OrderHistDto)를 생성
		for (Reservation reservation : reservations) {
			ReservationHistDto reservationHistDto = new ReservationHistDto(reservation);
			List<ReservationMovie> reservationMovies = reservation.getReservationMovies();

			for (ReservationMovie reservationMovie : reservationMovies) {
				// 상품의 대표이미지 가져오기
				MovieImg movieImg = movieImgRepository.findByMovieIdAndRepimgYn(reservationMovie.getMovie().getId(),
						"Y");
				ReservationMovieDto reservationMovieDto = new ReservationMovieDto(reservationMovie,
						movieImg.getImgUrl());
				reservationHistDto.addReservationMovieDto(reservationMovieDto);
			}

			reservationHistDtos.add(reservationHistDto);
		}

		// 4. 페이지 구현 객체를 생성하여 return
		return new PageImpl<>(reservationHistDtos, pageable, totalCount);
	}

	// 본인확인(현재 로그인한 사용자와 주문데이터를 생성한 사용자가 같은지 검사)
	@Transactional
	public boolean validateReservation(Long reservationId, String email) {
		Member curMember = memberRepository.findByEmail(email); // 로그인한 사용자 찾기
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new); // 주문내역

		Member savedMember = reservation.getMember(); // 주문한 사용자 찾기

		// 로그인한 사용자의 이메일과 주문한 사용자의 이메일이 같은지 최종 비교
		if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			// 같지 않으면
			return false;
		}

		return true;
	}

	// 주문 삭제
	public void deleteReservation(Long reservationId) {
		// ★delete하기 전에 select를 한번 해준다
		// ->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);

		// delete
		reservationRepository.delete(reservation);
	}
}
