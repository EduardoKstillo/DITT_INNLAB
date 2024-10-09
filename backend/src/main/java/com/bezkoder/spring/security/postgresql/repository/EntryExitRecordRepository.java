package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.EntryExitRecord;
import com.bezkoder.spring.security.postgresql.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryExitRecordRepository extends JpaRepository<EntryExitRecord, Long> {

}
