package com.cinema.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.cinema.dto.MovieFormDto;
import com.cinema.dto.MovieImgDto;
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
import com.cinema.repository.ReservationMovieRepository;
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
	private final ReservationMovieRepository reservationMovieRepository;
	private final MovieImgRepository movieImgRepository;

	// 주문하기
	public Long reservation(ReservationDto reservationDto, String email) {

		// 1. 주문할 상품을 조회
		Movie movie = movieRepository.findById(reservationDto.getMovieId()).orElseThrow(EntityNotFoundException::new);

		// 2. 현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);

		// 3. 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
		List<ReservationMovie> reservationMovieList = new ArrayList<>();
		ReservationMovie reservationMovie = ReservationMovie.createReservationMovie(movie, reservationDto.getCount(),
				reservationDto.getSeat(), reservationDto.getDatePicker(), reservationDto.getSelectTime());

		reservationMovieList.add(reservationMovie);
		reservationMovieList.get(0).getSeat();
		// 4. 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
		Reservation reservation = Reservation.createReservation(member, reservationMovieList);

		reservationRepository.save(reservation); // insert

		return reservation.getId();
	}

	// 주문 목록을 가져오는 서비스
	@Transactional(readOnly = true)
	public List<ReservationHistDto> getReservationList(String email) {
		// 1. 유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회
		List<Reservation> reservations = reservationRepository.findReservations(email);

		List<ReservationHistDto> reservationHistDtos = new ArrayList<>();

		// 목표 : member_id
		// 1. email을 가지고 DB에있는 Member를 찾아와
		Member member = memberRepository.findByEmail(email);

		// 2. 찾아온 member.id를 넣고
		List<ReservationMovie> reservationMovies = reservationMovieRepository.getReservationMovieList(member.getId());

		// DB에 있는
		// 3. 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO(OrderHistDto)를 생성
		for (Reservation reservation : reservations) {
//			List<ReservationMovie> reservationMovies = reservation.getReservationMovies();
			for (ReservationMovie reservationMovie : reservationMovies) {
				ReservationHistDto reservationHistDto = new ReservationHistDto(reservation, reservationMovie);

				// 상품의 대표이미지 가져오기
				MovieImg movieImg = movieImgRepository.findByMovieIdAndRepimgYn(reservationMovie.getMovie().getId(),
						"Y");
				ReservationMovieDto reservationMovieDto = new ReservationMovieDto(reservationMovie,
						movieImg.getImgUrl());
				reservationHistDto.addReservationMovieDto(reservationMovieDto);

				reservationHistDtos.add(reservationHistDto);
			}
		}
		// 4. 페이지 구현 객체를 생성하여 return
		return reservationHistDtos;
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

	// 예매정보 가져오기
	@Transactional(readOnly = true) // 트랙잰션 읽기 전용(변경감지 수해하지 않음) ->성능 향상
	public ReservationHistDto getReservationHistDtl(Long reservationId, String email) {

		List<Reservation> reservations = reservationRepository.findReservations(email);

		// 2.movie테이블에 있는데이터를 가져온다.
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);

		// Movie 엔티티 객체 -> dto로 변환
		ReservationHistDto reservationHistDto = ReservationHistDto.of(reservation);

		// 3.MovieFormDto에 이미지 정보(movieImgDtoList)를 넣어준다.

		return reservationHistDto;

	}

	// 주문 취소
	public void cancelReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(EntityNotFoundException::new);
		// OrderStatus를 update -> entity의 필드 값을 바꿔주면 된다.
		reservation.cancelReservation();
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

	// 예매한 영화의 정보를 가져온다
	public ReservationMovie reserveMovieinFo(Long id) {
		return reservationMovieRepository.reserveMovieinFo(id);
	}

	public Reservation getMovie(Long id) {
		return reservationRepository.reserveMovieinFo(id);
	}

}
