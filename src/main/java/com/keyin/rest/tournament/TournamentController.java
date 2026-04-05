package com.keyin.rest.tournament;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.keyin.rest.member.Member;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        Tournament created = tournamentService.createTournament(tournament);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Return participants (members) for a specific tournament
    @GetMapping("/{id}/members")
    public ResponseEntity<Set<Member>> getTournamentMembers(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(t -> ResponseEntity.ok(t.getMembers()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> searchTournaments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long memberId
    ) {
        if (startDate != null) {
            return ResponseEntity.ok(tournamentService.findByStartDate(startDate));
        }
        if (location != null) {
            return ResponseEntity.ok(tournamentService.findByLocation(location));
        }
        if (memberId != null) {
            return ResponseEntity.ok(tournamentService.findByMemberId(memberId));
        }
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        try {
            Tournament updated = tournamentService.updateTournament(id, tournament);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    // Add/remove member endpoints
    @PostMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<Tournament> addMember(@PathVariable Long tournamentId, @PathVariable Long memberId) {
        Tournament updated = tournamentService.addMemberToTournament(tournamentId, memberId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<Tournament> removeMember(@PathVariable Long tournamentId, @PathVariable Long memberId) {
        Tournament updated = tournamentService.removeMemberFromTournament(tournamentId, memberId);
        return ResponseEntity.ok(updated);
    }
}
