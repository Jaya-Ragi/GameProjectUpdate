package com.marvelous.games.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marvelous.games.models.Character;
import com.marvelous.games.models.Marvelous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class MClients {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    ObjectMapper mapper;


    String avengers = "http://www.mocky.io/v2/5ecfd5dc3200006200e3d64b";
    String anti_heroes = "http://www.mocky.io/v2/5ecfd630320000f1aee3d64d";
    String mutants = "http://www.mocky.io/v2/5ecfd6473200009dc1e3d64e";

    @Async
    public Marvelous getAvengersCharacter() {
        ResponseEntity<String> responseCharacter = restTemplate.exchange(avengers, HttpMethod.GET, null, String.class);
        File avengersFile = null;
        try {
            avengersFile = ResourceUtils.getFile("classpath:marvelous/avengers.ser");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Marvelous antiHeroesList = busineesRules(responseCharacter, avengersFile);

        return antiHeroesList;
    }

    @Async
    public Marvelous getAntiHeroesCharacter() {
        ResponseEntity<String> responseCharacter = restTemplate.exchange(anti_heroes, HttpMethod.GET, null, String.class);
        File antiHeroesFile = null;
        try {
            antiHeroesFile = ResourceUtils.getFile("classpath:marvelous/antiHeroes.ser");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Marvelous antiHeroesList = busineesRules(responseCharacter, antiHeroesFile);
        return antiHeroesList;
    }


    @Async
    public Marvelous getMutants() {
        ResponseEntity<String> responseCharacter = restTemplate.exchange(mutants, HttpMethod.GET, null, String.class);
        File mutantsFile = null;
        try {
            mutantsFile = ResourceUtils.getFile("classpath:marvelous/mutants.ser");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Marvelous antiHeroesList = busineesRules(responseCharacter, mutantsFile);
        return antiHeroesList;
    }


    public Marvelous busineesRules(ResponseEntity<String> responseAntiHeroesCharacter, File filePath) {
        List<Character> updateCharacter = null;
        FileOutputStream fos =null;
        ObjectOutputStream oos=null;
        Marvelous updateMarvelous = new Marvelous();

        if (responseAntiHeroesCharacter != null && responseAntiHeroesCharacter.hasBody()) {
            String response = responseAntiHeroesCharacter.getBody();
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            JsonArray jsonArr = jsonObject.get("character").getAsJsonArray();
            List<Character> newCharacterList = new ArrayList<Character>();
            for (Object entry : jsonArr) {
                Character character = new Character();
                JsonObject jObject = new JsonParser().parse(entry.toString()).getAsJsonObject();
                character.setName(jObject.get("name").getAsString());
                character.setMax_power(jObject.get("max_power").getAsString());
                newCharacterList.add(character);
            }
            Marvelous marvelous = new Marvelous();
            marvelous.setName(jsonObject.get("name").getAsString());
            marvelous.setCharacter(newCharacterList);
            updateMarvelous = marvelous;
            try {
            FileInputStream fis = new FileInputStream(filePath);
            Marvelous marvelousFromFile=null;
            List<Character> oldCharacter=null;
            if(filePath.exists() && filePath.length() != 0) {
                fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                marvelousFromFile = (Marvelous) ois.readObject();
                oldCharacter = marvelousFromFile.getCharacter();
                ois.close();
            }else if(oldCharacter == null && marvelous!=null) {
                     fos = new FileOutputStream(filePath);
                     oos = new ObjectOutputStream(fos);
                     oos.writeObject(marvelous);
                     oos.close();
                } else {
                    for (Character newChar : newCharacterList) {
                        for (Character oldChar : oldCharacter) {
                            Character character = new Character();
                            if (newChar.getName().equals(oldChar.getName()) && !newChar.getMax_power().equals(oldChar.getMax_power())) {
                                character.setName(newChar.getName());
                                character.setMax_power(newChar.getMax_power());
                            }
                            if (!newChar.getName().equals(oldChar.getName())) {
                                String min_power = "0";
                                int power = Math.min(Integer.parseInt(min_power), Integer.parseInt(oldChar.getMax_power()));
                                character.setMax_power(String.valueOf(newChar.getMax_power()));
                                character.setName(newChar.getName());
                            }
                            updateCharacter.add(character);
                        }
                    }
                    updateMarvelous.setName(marvelousFromFile.getName());
                    updateMarvelous.setCharacter(updateCharacter);
                    if(updateMarvelous!=null){
                        fos = new FileOutputStream(filePath);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(updateMarvelous);
                        oos.close();
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (EOFException exception) {
                exception.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return updateMarvelous;
    }
}

