package com.keyin.rest.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private Member sample;

    @BeforeEach
    void setUp() {
        sample = new Member("Alice Smith", "123 Golf Lane", "alice@example.com", "REGULAR",
                "555-0100", LocalDate.of(2024, 5, 1), 12);
        sample.setId(1L);
    }

    @Test
    void getMemberReturnsOkAndMemberBody() {
        when(memberService.getMemberById(1L)).thenReturn(Optional.of(sample));

        ResponseEntity<Member> resp = memberController.getMember(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1L, resp.getBody().getId());
        assertEquals("Alice Smith", resp.getBody().getName());
    }

    @Test
    void getMemberReturnsNotFoundWhenMissing() {
        when(memberService.getMemberById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Member> resp = memberController.getMember(99L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody());
    }

    @Test
    void createMemberReturnsCreatedAndBody() {
        Member toCreate = new Member("Bob", "addr", "bob@example.com", "VIP", "555-0001", LocalDate.now(), 6);
        Member created = new Member();
        created.setId(2L);

        when(memberService.createMember(any(Member.class))).thenReturn(created);

        ResponseEntity<Member> resp = memberController.createMember(toCreate);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(2L, resp.getBody().getId());
    }
}
