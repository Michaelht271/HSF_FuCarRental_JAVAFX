package com.michael.fu.hsf301assigment1.service.impl;

import com.michael.fu.hsf301assigment1.entity.Car;
import com.michael.fu.hsf301assigment1.repository.CarRepository;
import com.michael.fu.hsf301assigment1.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService extends BaseService {
        private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> findAll(){
        return carRepository.findAll();
    }

    public void save(Car car) {
       carRepository.save(car);
    }


    public void update(Car car) {
        carRepository.save(car);
    }

    public Car findById(Long carId) {
        return carRepository.findById(carId).orElse(null);
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }
}
