package com.example.ligma.BE;

import java.util.ArrayList;

public class Player {
    private String name;
    private String image;
    private ArrayList<Card> inventory;
    private ArrayList<Card> statuses;

    public Player(String name, ArrayList<Card> inventory, String image) {
        this.name = name;
        this.inventory = inventory;
        this.image = image;
        this.statuses = new ArrayList<>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Card> inventory) {
        this.inventory = inventory;
    }

    public void addToInventory(Card card) {
        inventory.add(card);
    }

    public void removeFromInventory(Card card) {
        inventory.remove(card);
    }

    public ArrayList<Card> getStatuses() { return statuses; }

    public void addToStatuses(Card card) { statuses.add(card); }

    public void removeFromStatuses(Card card) { statuses.remove(card); }
}
