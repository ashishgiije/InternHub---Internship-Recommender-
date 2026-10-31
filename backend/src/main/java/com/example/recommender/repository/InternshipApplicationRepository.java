package com.example.recommender.repository;

import com.example.recommender.entity.InternshipApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {

    List<InternshipApplication> findByUserIdOrderByAppliedAtDesc(Integer userId);

    List<InternshipApplication> findAllByOrderByAppliedAtDesc();

    boolean existsByUserIdAndInternshipId(Integer userId, Integer internshipId);

    java.util.Optional<InternshipApplication> findById(Integer id);
}
