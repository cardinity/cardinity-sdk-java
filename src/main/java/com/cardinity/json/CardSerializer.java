package com.cardinity.json;

import com.cardinity.model.Card;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CardSerializer implements JsonSerializer<Card> {

    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pan", card.getPan());
        jsonObject.addProperty("exp_year", card.getExpYear());
        jsonObject.addProperty("exp_month", card.getExpMonth());
        jsonObject.addProperty("cvc", String.format("%03d", card.getCvc()));
        jsonObject.addProperty("holder", card.getHolder());

        return jsonObject;
    }
}
