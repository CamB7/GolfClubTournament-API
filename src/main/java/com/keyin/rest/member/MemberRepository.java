package com.keyin.rest.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameContainingIgnoreCase(String name);
    List<Member> findByPhoneNumber(String phoneNumber);
    List<Member> findByMembershipType(String membershipType);
    List<Member> findByMembershipStartDate(LocalDate startDate);
}
