package com.example.basicrestapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pointtransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Double exchangeRate;
    private long userId;

    // NOTE - This is a place-holder.  If, in fact more information about
    // the source of funds is needed, we can put it here.
    private String source;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String destination;
    private long amount;
    private long startingBalance;
    private long endingBalance;
    private String status;

    public Pointtransfer() {
    }

    public Pointtransfer(long id, long userId, String source, String destination, String message, long amount, long startingBalance, long endingBalance, String status, Double exchangeRate) {
        this.id = id;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.message = message;
        this.amount = amount;
        this.startingBalance = startingBalance;
        this.endingBalance = endingBalance;
        this.exchangeRate = exchangeRate;
        this.status = status;
    }

    public Pointtransfer(String message, long amount) {
        this.message = message;
        this.amount = amount;
    }

    public Pointtransfer(long userId, FundingRequest fundingRequest) {
        this.userId=userId;
        this.amount=fundingRequest.getAmount();
        this.message="funding";
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String messsage) {
        this.message = messsage;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setDatetime(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    @JsonIgnore
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(long startingBalance) {
        this.startingBalance = startingBalance;
    }

    public long getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(long endingBalance) {
        this.endingBalance = endingBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
