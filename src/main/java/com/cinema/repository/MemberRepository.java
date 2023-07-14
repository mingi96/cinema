package com.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	// select * from member where email = ?
	Member findByEmail(String email);
}
