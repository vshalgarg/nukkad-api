package com.neepanlokInfotech.nukkad_App.repositories;

import com.neepanlokInfotech.nukkad_App.entities.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity,Long> {
    Optional<OtpEntity> findTopByMobileNumberOrderByCreatedAtDesc (String mobileNumber);
}
