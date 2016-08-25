package com.innouvous.balancetracker.data;

import com.innouvous.utils.Utils;

import java.util.Date;

/**
 * Created by Allan on 8/21/2016.
 */
public class Provider {
    private Long id;
    private String name;

    private Date lastUsed = null;
    private double balance;
    private double fare;
    private String unit;

    public static void validateProvider(Provider provider) throws Exception {
        validateAmount(provider.getBalance(), null);
        validateAmount(provider.getFare(), null);
        if (Utils.isNullOrEmpty(provider.getName()))
            throw new Exception("Name cannot be empty");
    }

    private static void validateAmount(Double amount, Object valueType) throws Exception {
        if (amount < 0)
            throw new Exception("The amount cannot be negative");
    }

    public Provider(String name, double balance, double fare)
    {
        this.name= name;
        this.balance = balance;
        this.fare = fare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
