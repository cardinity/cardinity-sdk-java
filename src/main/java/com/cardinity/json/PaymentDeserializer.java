package com.cardinity.json;

import com.cardinity.exceptions.CardinityClientException;
import com.cardinity.model.Card;
import com.cardinity.model.Payment;
import com.cardinity.model.PaymentInstrument;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class PaymentDeserializer implements JsonDeserializer<Payment> {

    private static final String PAYMENT_METHOD_PROP = "payment_method";
    private static final String PAYMENT_INSTRUMENT_PROP = "payment_instrument";
    // @formatter:off
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new UtcDateTypeAdapter())
            .create();
    // @formatter:on

    @Override
    public Payment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {

        JsonObject paymentObject = json.getAsJsonObject();
        String paymentMethod = paymentObject.getAsJsonPrimitive(PAYMENT_METHOD_PROP).getAsString();

        Class<?> clazz;
        if (paymentMethod.equals("card")) {
            clazz = Card.class;
        } else {
            throw new CardinityClientException("DeserializationException: invalid payment method");
        }

        PaymentInstrument instrument = context.deserialize(paymentObject.get(PAYMENT_INSTRUMENT_PROP), clazz);
        paymentObject.remove(PAYMENT_INSTRUMENT_PROP);
        Payment payment = GSON.fromJson(paymentObject, Payment.class);

        payment.setPaymentInstrument(instrument);

        return payment;
    }
}
