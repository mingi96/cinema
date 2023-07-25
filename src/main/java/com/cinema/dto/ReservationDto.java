package com.cinema.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class ReservationDto {
	@NotNull(message = "상품 아이디는 필수 입력입니다.")
	private Long movieId;

	@Min(value = 1, message = "최소 주문수량은 1개 입니다")
	@Max(value = 999, message = "최대 주문수량은 999개 입니다")
	private int count;

	private List<String> seat;
}
