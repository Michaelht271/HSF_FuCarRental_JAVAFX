package com.michael.fu.hsf301assigment2.service.impl;

import com.michael.fu.hsf301assigment2.entity.Producer;
import com.michael.fu.hsf301assigment2.repository.ProducerRepository;
import com.michael.fu.hsf301assigment2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService extends BaseService {
    private final ProducerRepository producerRepository;

    @Autowired
    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    public Producer findById(Long producerId) {
        return producerRepository.findById(producerId).orElse(null);
    }
}
