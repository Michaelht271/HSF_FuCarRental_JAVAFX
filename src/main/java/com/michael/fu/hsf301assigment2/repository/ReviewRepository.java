package com.michael.fu.hsf301assigment2.repository;

import com.michael.fu.hsf301assigment2.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
