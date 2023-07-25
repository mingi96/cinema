package com.cinema.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation_movie")
@Setter
@Getter
@ToString
public class ReservationMovie extends BaseEntity {
	@Id
	@Column(name = "reservation_movie_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	private Movie movie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	private int reservationPrice; // 주문가격

	private int count; // 수량

	@ElementCollection
	private List<String> seat;

	// 주문할 상품하고 주문 수량을 통해 orderItem객체를 만듬
	public static ReservationMovie createReservationMovie(Movie movie, int count) {
		ReservationMovie reservationMovie = new ReservationMovie();
		reservationMovie.setMovie(movie);
		reservationMovie.setCount(count);
		reservationMovie.setReservationPrice(movie.getPrice());
		reservationMovie.setSeat(reservationMovie.getSeat());

		return reservationMovie;
	}

	public int getTotalPrice() {
		return reservationPrice * count;
	}

}
