package com.example.basicrestapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class FundingResponse {
    private long id;
    private String message;
    private StatusName status;
    private long startingBalance;
    private long amount;
    private long endingBalance;

    private String destination;


    public FundingResponse() {
    }

    public FundingResponse(long id, String message) {
        this.id = id;
        this.message = message;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public long getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(long startingBalance) {
        this.startingBalance=startingBalance;
    }

    public long getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(long endingBalance) {
        this.endingBalance=endingBalance;
    }

    public StatusName getStatus() {
        return status;
    }

    public void setStatus(StatusName status) {
        this.status = status;
    }


    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
