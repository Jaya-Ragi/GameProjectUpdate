package com.marvelous.games.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Character implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;

    private String max_power;

    public Character(String name, String max_power,Integer firstTime) {
        this.name = name;
        this.max_power = max_power;
    }

    public Character(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMax_power() {
        return max_power;
    }

    public void setMax_power(String max_power) {
        this.max_power = max_power;
    }
}
