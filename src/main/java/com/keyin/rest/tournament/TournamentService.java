package com.keyin.rest.tournament;

import com.keyin.rest.member.Member;
import com.keyin.rest.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final MemberRepository memberRepository;

    public TournamentService(TournamentRepository tournamentRepository, MemberRepository memberRepository) {
        this.tournamentRepository = tournamentRepository;
        this.memberRepository = memberRepository;
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament updateTournament(Long id, Tournament updated) {
        return tournamentRepository.findById(id).map(existing -> {
            existing.setStartDate(updated.getStartDate());
            existing.setEndDate(updated.getEndDate());
            existing.setLocation(updated.getLocation());
            existing.setEntryFee(updated.getEntryFee());
            existing.setCashPrizeAmount(updated.getCashPrizeAmount());
            return tournamentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Tournament not found: " + id));
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    // Search
    public List<Tournament> findByStartDate(LocalDate date) {
        return tournamentRepository.findByStartDate(date);
    }

    public List<Tournament> findByLocation(String location) {
        return tournamentRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Tournament> findByMemberId(Long memberId) {
        return tournamentRepository.findByMemberId(memberId);
    }

    // Manage participants
    public Tournament addMemberToTournament(Long tournamentId, Long memberId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found: " + tournamentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

        tournament.addMember(member);
        return tournamentRepository.save(tournament);
    }

    public Tournament removeMemberFromTournament(Long tournamentId, Long memberId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found: " + tournamentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

        tournament.removeMember(member);
        return tournamentRepository.save(tournament);
    }
}
