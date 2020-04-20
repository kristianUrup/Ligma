package com.example.ligma.BE;

public class Card {
    private int id;
    private CardType cardType;
    private String text;

    public Card(int id, CardType cardType, String text) {
        this.id = id;
        this.cardType = cardType;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
