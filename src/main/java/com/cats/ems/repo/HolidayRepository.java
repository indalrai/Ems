package com.cats.ems.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cats.ems.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long>{
	
	
}
