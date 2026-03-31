package com.keyin.rest.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member sample;

    @BeforeEach
    void setUp() {
        sample = new Member("Alice Smith", "123 Golf Lane", "alice@example.com", "REGULAR",
                "555-0100", LocalDate.of(2024, 5, 1), 12);
        sample.setId(1L);
    }

    @Test
    void createMemberSavesAndReturns() {
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member m = invocation.getArgument(0);
            m.setId(2L);
            return m;
        });

        Member toCreate = new Member("Bob", "addr", "bob@example.com", "VIP", "555-0001", LocalDate.now(), 6);
        Member created = memberService.createMember(toCreate);

        assertThat(created.getId()).isEqualTo(2L);
        assertThat(created.getEmail()).isEqualTo("bob@example.com");
        verify(memberRepository, times(1)).save(toCreate);
    }

    @Test
    void getMemberByIdReturnsMemberWhenFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sample));

        Optional<Member> found = memberService.getMemberById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice Smith");
        verify(memberRepository).findById(1L);
    }

    @Test
    void updateMemberUpdatesAndReturns() {
        Member updated = new Member("Alice S.", "new addr", "alice.smith@example.com", "VIP", "555-0100", LocalDate.of(2024,5,1), 24);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member result = memberService.updateMember(1L, updated);

        assertThat(result.getName()).isEqualTo("Alice S.");
        assertThat(result.getMembershipType()).isEqualTo("VIP");
        verify(memberRepository).findById(1L);
        verify(memberRepository).save(sample);
    }

    @Test
    void updateMemberNotFoundThrows() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());
        Member updated = new Member();
        assertThrows(RuntimeException.class, () -> memberService.updateMember(99L, updated));
        verify(memberRepository).findById(99L);
    }

    @Test
    void findByNameDelegatesToRepo() {
        when(memberRepository.findByNameContainingIgnoreCase("alice")).thenReturn(List.of(sample));
        List<Member> results = memberService.findByName("alice");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("alice@example.com");
        verify(memberRepository).findByNameContainingIgnoreCase("alice");
    }
}
