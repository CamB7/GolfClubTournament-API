package com.keyin.rest.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStartDate(LocalDate startDate);
    List<Tournament> findByLocationContainingIgnoreCase(String location);

    @Query("select t from Tournament t join t.members m where m.id = :memberId")
    List<Tournament> findByMemberId(@Param("memberId") Long memberId);
}
