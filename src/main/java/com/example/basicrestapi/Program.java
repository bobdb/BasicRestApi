package com.example.basicrestapi;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private double exchangeRate;

    public Program() {
    }

    public Program(long id, String name, double exchangeRate) {
        this.id = id;
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public Program(String name, double exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
