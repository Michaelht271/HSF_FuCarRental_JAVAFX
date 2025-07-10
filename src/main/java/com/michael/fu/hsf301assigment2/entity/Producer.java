package com.michael.fu.hsf301assigment2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name ="Producer")
@NoArgsConstructor
@Getter
@Setter

public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProducerID")
    private Long producerId;
    @Column(name= "ProducerName", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String producerName;

    @Column(name ="Address", columnDefinition = "NVARCHAR(100)")
    private String address;

    @Column(name ="Contry", columnDefinition = "NVARCHAR(20)")
    private String country;

    @OneToMany(mappedBy ="producer",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Car> cars;

    public void addCar(Car car){
        cars.add(car);
        car.setProducer(this);
    }

}
