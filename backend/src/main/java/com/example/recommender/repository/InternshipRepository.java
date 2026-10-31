package com.example.recommender.repository;

import com.example.recommender.entity.Internship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipRepository extends JpaRepository<Internship, Integer> {

    List<Internship> findByCategoryIgnoreCase(String category);
}
