package com.keyin.rest.member;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL-friendly auto increment
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    private String membershipType;

    private String phoneNumber;

    private LocalDate membershipStartDate;

    private Integer membershipDurationMonths;

    // Bi-directional many-to-many with Tournament
    @ManyToMany
    @JoinTable(
            name = "tournament_participants",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private Set<com.keyin.rest.tournament.Tournament> tournaments = new HashSet<>();

    public Member() {
    }

    public Member(String name, String address, String email, String membershipType, String phoneNumber,
                  LocalDate membershipStartDate, Integer membershipDurationMonths) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.membershipType = membershipType;
        this.phoneNumber = phoneNumber;
        this.membershipStartDate = membershipStartDate;
        this.membershipDurationMonths = membershipDurationMonths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public Integer getMembershipDurationMonths() {
        return membershipDurationMonths;
    }

    public void setMembershipDurationMonths(Integer membershipDurationMonths) {
        this.membershipDurationMonths = membershipDurationMonths;
    }

    public Set<com.keyin.rest.tournament.Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(Set<com.keyin.rest.tournament.Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public void addTournament(com.keyin.rest.tournament.Tournament tournament) {
        this.tournaments.add(tournament);
        // inverse side is updated by service layer to avoid circular diagnostic issues in the IDE
    }

    public void removeTournament(com.keyin.rest.tournament.Tournament tournament) {
        this.tournaments.remove(tournament);
        // inverse side is updated by service layer
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
