package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cinema.constant.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Table(name = "reservations")
@Getter
@ToString
public class Reservation {

	@Id
	@Column(name = "reservation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDateTime reservationDate; // 예매날짜

	private LocalDateTime reservationTime; // 상영시간

	@Enumerated(EnumType.STRING)
	private ReservationStatus reservationStatus; // 주문상태

	private LocalDateTime rs_regdate; // 생성시간

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	// order에서도 orderItem을 참조할 수 있도록 양방향 관계를 만든다.
	// 다만 orderItem은 자식 테이블이 되므로 List로 만든다.
	// 연관관계의 주인 설정(외래키 지정)
	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

	private List<ReservationMovie> reservationMovies = new ArrayList<>();

	public void addReservationMovie(ReservationMovie reservationMovie) {
		this.reservationMovies.add(reservationMovie);
		reservationMovie.setReservation(this); // ★양방향 참조관계 일때는 reservationMovie객체에도 Reservation 객체를 세팅한다.
	}

	// reservation 객체를 생성해준다.
	public static Reservation createReservation(Member member, List<ReservationMovie> reservationMovieList) {
		Reservation reservation = new Reservation();
		reservation.setMember(member);

		for (ReservationMovie reservationMovie : reservationMovieList) {
		
			reservation.addReservationMovie(reservationMovie);
		}

		reservation.setReservationStatus(ReservationStatus.RESERVATION);
		reservation.setReservationDate(LocalDateTime.now());
		

		return reservation;
	}

	// 총 주문 금액
	public int getTotalPrice() {
		int totalPrice = 0;
		for (ReservationMovie reservationMovie : reservationMovies) {
			totalPrice += reservationMovie.getTotalPrice();
		}
		return totalPrice;
	}

//	// 주문 취소
//	public void cancelReservation() {
//		this.reservationStatus = ReservationStatus.CANCEL;
//
//		// 재고를 원래대로 돌려 놓는다.
//		for (ReservationMovie reservationMovie : reservationMovies) {
//			reservationMovie.cancel();
//		}
//	}

}
