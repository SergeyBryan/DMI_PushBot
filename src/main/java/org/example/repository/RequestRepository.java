package org.example.repository;

import org.example.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Modifying
    @Query(value = "DELETE FROM Request r WHERE r.createdDate < :date")
    void deleteRequestsByWeek(@Param("date") LocalDateTime date);

}