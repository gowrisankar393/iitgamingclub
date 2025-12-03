package com.example.iitgamingclub.model;

public enum Role {
    STRATEGIST, ATTACKER, DEFENDER, SUPPORTER, COORDINATOR;

    @Override
    public String toString() {
        //capitalize first letter
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}