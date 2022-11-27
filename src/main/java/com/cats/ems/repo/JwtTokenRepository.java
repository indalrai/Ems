package com.cats.ems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cats.ems.model.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{

	JwtToken findByEmployeeId(long token);

}
