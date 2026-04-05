package com.keyin.rest.tournament;

import com.keyin.exception.ResourceNotFoundException;
import com.keyin.rest.member.Member;
import com.keyin.rest.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament sampleTournament;
    private Member sampleMember;

    @BeforeEach
    void setUp() {
        sampleTournament = new Tournament(LocalDate.of(2026,6,15), LocalDate.of(2026,6,16), "River View", new BigDecimal("50"), new BigDecimal("500"));
        sampleTournament.setId(1L);

        sampleMember = new Member();
        sampleMember.setId(1L);
        sampleMember.setName("Alice");
    }

    @Test
    void createTournamentSaves() {
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            t.setId(2L);
            return t;
        });

        Tournament created = tournamentService.createTournament(new Tournament());
        assertThat(created.getId()).isEqualTo(2L);
        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    void addMemberToTournamentSuccess() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tournament updated = tournamentService.addMemberToTournament(1L, 1L);

        assertThat(sampleTournament.getMembers()).hasSize(1);
        assertThat(sampleTournament.getMembers().iterator().next().getId()).isEqualTo(1L);

        assertThat(updated.getMembers()).hasSize(1);

        verify(tournamentRepository, times(2)).findById(1L);
        verify(memberRepository).findById(1L);
        // service saves the owning side (Member); accept any Member instance
        verify(memberRepository).save(any(Member.class));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void addMemberToTournamentNotFoundThrows() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tournamentService.addMemberToTournament(99L,1L));
        verify(tournamentRepository).findById(99L);
    }

    @Test
    void findByMemberIdDelegatesToRepo() {
        when(tournamentRepository.findByMemberId(1L)).thenReturn(List.of(sampleTournament));
        List<Tournament> results = tournamentService.findByMemberId(1L);
        assertThat(results).hasSize(1);
        verify(tournamentRepository).findByMemberId(1L);
    }
}
