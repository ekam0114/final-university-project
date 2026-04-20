package com.example.University.repository;


import com.example.University.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndWorkshopId(Long userId, Long workshopId);
    int countByWorkshopId(Long workshopID);

    Optional<Registration> findByUserIdAndWorkshopId(Long userId, Long workshopId);

    List<Registration> findByUserId(Long userId);

    List<Registration> findByWorkshopId(Long workshopId);

    List<Registration> findByUserIdAndStatus(Long userId, String status);

    List<Registration> findByWorkshopIdAndStatus(Long workshopId, String status);
}
