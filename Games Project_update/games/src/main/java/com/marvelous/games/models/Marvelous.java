package com.marvelous.games.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Marvelous implements Serializable{

    private static final long serialVersionUID = 1L;
    private List<Character> character;
    private String name;
    public Marvelous(){};
    public Marvelous(List<Character> character, String name) {
        this.character = character;
        this.name = name;
    }
    public List<Character> getCharacter() {
        return character;
    }

    public void setCharacter(List<Character> character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
