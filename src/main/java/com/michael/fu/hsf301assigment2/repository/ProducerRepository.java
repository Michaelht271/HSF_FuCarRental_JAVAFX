package com.michael.fu.hsf301assigment2.repository;

import com.michael.fu.hsf301assigment2.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
}

















