package com.gecm.lostfound.repository;

import com.gecm.lostfound.model.Claim;
import com.gecm.lostfound.model.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    long countByStatus(ClaimStatus status);
}
