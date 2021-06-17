package com.example.basicrestapi;

public class FundingRequest {
    private long userId;
    private long amount;
    private String message;

    public FundingRequest() {
    }

    public FundingRequest(long userId, long amount, String message) {
        this.userId = userId;
        this.amount = amount;

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
