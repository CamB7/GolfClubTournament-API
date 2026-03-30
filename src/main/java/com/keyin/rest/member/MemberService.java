package com.keyin.rest.member;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member updateMember(Long id, Member updated) {
        return memberRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setEmail(updated.getEmail());
            existing.setMembershipType(updated.getMembershipType());
            existing.setPhoneNumber(updated.getPhoneNumber());
            existing.setMembershipStartDate(updated.getMembershipStartDate());
            existing.setMembershipDurationMonths(updated.getMembershipDurationMonths());
            return memberRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // Search methods
    public List<Member> findByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Member> findByPhone(String phone) {
        return memberRepository.findByPhoneNumber(phone);
    }

    public List<Member> findByMembershipType(String type) {
        return memberRepository.findByMembershipType(type);
    }

    public List<Member> findByMembershipStartDate(LocalDate date) {
        return memberRepository.findByMembershipStartDate(date);
    }
}
