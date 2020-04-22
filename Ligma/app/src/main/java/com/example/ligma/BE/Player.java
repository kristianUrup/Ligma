package com.example.ligma.BE;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> inventory;

    public Player(String name, ArrayList<Card> inventory) {
        this.name = name;
        this.inventory = inventory;
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
}
