package com.cats.ems.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cats.ems.model.CredentialManager;

public interface CredentialsRepository extends JpaRepository<CredentialManager, Integer> {
	CredentialManager findByLoginId(String userId);
}
