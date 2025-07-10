package com.michael.fu.hsf301assigment2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="Account")
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="AccountId")
    private Long accountId;

    @Column(name="AccountName", columnDefinition = "NVARCHAR(20)", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name="Role", columnDefinition = "NVARCHAR(10)", nullable = false)
    private Role role;


    public Account(String accountName, Role role) {
        this.accountName = accountName;
        this.role = role;
    }

    @OneToOne(mappedBy = "account")
    private Customer customer;
}
