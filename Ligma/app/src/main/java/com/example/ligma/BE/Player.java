package com.example.ligma.BE;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private String image;
    private ArrayList<Card> inventory;

    public Player(String name, ArrayList<Card> inventory, String image) {
        this.name = name;
        this.inventory = inventory;
        this.image = image;
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
}
