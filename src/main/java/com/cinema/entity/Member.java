package com.cinema.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.cinema.constant.Role;
import com.cinema.dto.MemberFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 엔티티 클래스로 정의
@Table(name = "member") // 테이블 이름 지정
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

	@Id
	@Column(name = "member_id") // 테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.AUTO) // 기본키를 자동으로 생성해주는 전략 사용
	private Long id;

	@Column(unique = true, length = 255) // 중복된 값이 들어올 수 없다.
	private String email;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 50)
	private String address;

	@Enumerated(EnumType.STRING)
	private Role role;// 역할

	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		// 패스워드 암호화
		String password = passwordEncoder.encode(memberFormDto.getPassword());

		// MemberFormDto를 -> Member 엔티티 객체로 변환

		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setPassword(password);
		// member.setRole(Role.ADMIN); //관리자로 가입할때
		member.setAddress(memberFormDto.getAddress());
		member.setRole(Role.ADMIN); // 일반 사용자로 가입할때

		return member;
	}
}
