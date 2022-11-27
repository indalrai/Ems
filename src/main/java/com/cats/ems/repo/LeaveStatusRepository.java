package com.cats.ems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cats.ems.model.LeaveStatus;

@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Long>{

}
