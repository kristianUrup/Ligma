package com.example.ligma.BE;

public class Card {
    private int id;
    private CardType cardType;
    private String text;
    private String effectExplanation;

    public Card(int id, CardType cardType, String text) {
        this.id = id;
        this.cardType = cardType;
        this.text = text;
    }

    public Card(int id, CardType cardType, String text, String effectExplanation) {
        this.id  = id;
        this.cardType = cardType;
        this.text = text;
        this.effectExplanation = effectExplanation;
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

    public String getEffectExplanation() {
        return effectExplanation;
    }

    public void setEffectExplanation(String effectExplanation) {
        this.effectExplanation = effectExplanation;
    }
}
