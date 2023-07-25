package com.cinema.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinema.dto.ReservationDto;
import com.cinema.dto.ReservationHistDto;
import com.cinema.dto.ReservationMovieDto;
import com.cinema.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@PostMapping(value = "/reservation")
	public @ResponseBody ResponseEntity reservation(@RequestBody @Valid ReservationDto reservationDto,
			BindingResult bindingResult, Principal principal) {
		// Principal: 로그인한 사용자의 정보를 가져올 수 있다.

		for(String st : reservationDto.getSeat()) {
			System.out.println(st + "5555");
		}
		
		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage()); // 에러메세지를 합친다.
			}

			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		System.out.println("222222222222");
		String email = principal.getName(); // id에 해당하는 정보를 가지고 온다(email)
		Long reservationId;

		try {
			reservationId = reservationService.reservation(reservationDto, email); // 주문하기
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Long>(reservationId, HttpStatus.OK);

	}

	// 주문내역을 보여준다.
	@GetMapping(value = { "/reservations", "/reservations/{page}" })
	public String reservationHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
		// 한페이지 당 4개의 데이터를 가지고 오도록 설정
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		// 2. 서비스 호출
		Page<ReservationHistDto> reservationHistDtoList = reservationService.getReservationList(principal.getName(),
				pageable);

		// 3. 서비스에서 가져온 값들을 view단에 model을 이용해 전송
		model.addAttribute("reservations", reservationHistDtoList);
		model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지
		// model.addAttribute("page", pageable.getPageNumber()); // 현재 페이지

		return "reservation/reservationHist";
	}

	// 주문 취소
	@PostMapping("/reservation/{reservationId}/cancel")
	public @ResponseBody ResponseEntity cancelReservation(@PathVariable("reservationId") Long reservationId,
			Principal principal) {
		// 1. 주문취소 권한이 있는지 확인(본인확인)
		if (!reservationService.validateReservation(reservationId, principal.getName())) {
			return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		// 2. 주문취소

		return new ResponseEntity<Long>(reservationId, HttpStatus.OK); // 성공했을때
	}

	// 주문삭제
	@DeleteMapping("/reservation/{reservationId}/delete")
	public @ResponseBody ResponseEntity deleteReservation(@PathVariable("reservationId") Long reservationId,
			Principal principal) {
		// 본인인증
		if (!reservationService.validateReservation(reservationId, principal.getName())) {
			return new ResponseEntity<String>("주문 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		// 2. 주문삭제
		reservationService.deleteReservation(reservationId);
		return new ResponseEntity<Long>(reservationId, HttpStatus.OK);
	}
}
