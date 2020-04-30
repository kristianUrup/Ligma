package com.example.ligma.BE;

public class Card {
    private  String cardSymbol;
    private String id;
    private CardType cardType;
    private FunctionType functionType;
    private String text;
    private String effectExplanation;

    public Card(){}

    public Card(String id, CardType cardType, String text) {
        this.id = id;
        this.cardType = cardType;
        this.text = text;
    }

    public Card(String id, CardType cardType, String text, String effectExplanation) {
        this.id  = id;
        this.cardType = cardType;
        this.text = text;
        this.effectExplanation = effectExplanation;
    }
    public Card(String id, CardType cardType, FunctionType functionType, String text, String effectExplanation, String cardSymbol) {
        this.id  = id;
        this.cardType = cardType;
        this.functionType = functionType;
        this.text = text;
        this.effectExplanation = effectExplanation;
        this.cardSymbol = cardSymbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

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

    public void setEffectExplanation(String effectExplanation) { this.effectExplanation = effectExplanation; }

    public String getCardSymbol() {return cardSymbol; }

    public void setCardSymbol(String cardSymbol) { this.cardSymbol = cardSymbol; }

    public FunctionType getFunctionType() { return functionType; }

    public void setFunctionType(FunctionType functionType) { this.functionType = functionType; }
}
