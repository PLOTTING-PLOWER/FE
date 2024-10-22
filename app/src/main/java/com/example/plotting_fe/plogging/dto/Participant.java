package com.example.plotting_fe.plogging.dto;

public class Participant {
    private String name;
    private String details;

    public Participant(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }
}