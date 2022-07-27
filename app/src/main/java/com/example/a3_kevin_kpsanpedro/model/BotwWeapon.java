package com.example.a3_kevin_kpsanpedro.model;

import java.util.List;

public class BotwWeapon {
    private int attack;
    private String category;
    private List<String>  common_locations;
    private int defense;
    private String description;
    private int id;
    private String image;
    private String name;

    public int getAttack() {
        return attack;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getCommon_locations() {
        return common_locations;
    }

    public int getDefense() {
        return defense;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BotwWeapon{" +
                "attack=" + attack +
                ", category='" + category + '\'' +
                ", common_location=" + common_locations +
                ", defense=" + defense +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                '}';
    }



}
