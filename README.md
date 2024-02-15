# Cardinity SDK Java 
[![Build Status](https://app.travis-ci.com/cardinity/cardinity-sdk-java.svg?branch=master)](https://app.travis-ci.com/cardinity/cardinity-sdk-java)

Java SDK for cardinity payment gateway. Library includes all the functionality provided by API. 

You can sign up for cardinity services - https://cardinity.com

## Requirements

Java 1.6 or later.

## Installation

### Maven

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.cardinity</groupId>
  <artifactId>cardinity-sdk-java</artifactId>
  <version>1.7.0</version>
</dependency>
```

### Gradle

Add this dependency to your project's build file:

```groovy
compile "com.cardinity:cardinity-sdk-java:1.7.0"
```

### Other

Manually install the following JARs:

* cardinity-sdk-java JAR from https://github.com/cardinity/cardinity-sdk-java/releases/latest
* [Google Gson](http://code.google.com/p/google-gson/) from <http://google-gson.googlecode.com/files/google-gson-2.2.4-release.zip>.

## Usage

### Initialize client object

```java
CardinityClient client = new CardinityClient(consumerKey, consumerSecret);
```

### Payments [API](https://developers.cardinity.com/api/v1/#payments)

#### Create new card payment

```java
Payment payment = new Payment();
payment.setAmount(new BigDecimal(10));
payment.setCurrency("EUR");
payment.setCountry("LT");
payment.setPaymentMethod(Payment.Method.CARD);
Card card = new Card();
card.setPan("4111111111111111");
card.setCvc(123);
card.setExpYear(2021);
card.setExpMonth(1);
card.setHolder("John Doe");
payment.setPaymentInstrument(card);

// Set threeds2Data to enable 3D Secure V2 flow
Threeds2Data threeds2Data = new Threeds2Data();
// Populate threeds2Data object with available data
payment.setThreeds2Data(threeds2Data);

Result<Payment> result = client.createPayment(payment);
```

#### Payment result handling

```java
Result<Payment> result = client.createPayment(payment);

/** Request was valid and payment was approved. */
if (result.isValid() && result.getItem().getStatus() == Payment.Status.APPROVED) {
    UUID paymentId = result.getItem().getId();
    // proceed with successful payment flow
}

/** Request was valid but payment requires additional authentication. */
else if (result.isValid() && result.getItem().getStatus() == Payment.Status.PENDING) {
    UUID paymentId = result.getItem().getId();
    String acsURL;
    String challengeRequest;
    if (result.getItem().isThreedsV2()) {
        // 3D Secure V2 flow
        acsURL = result.getItem().getThreeds2AuthorizationInformation().getAcsUrl();
        challengeRequest = result.getItem().getThreeds2AuthorizationInformation().getCReq();
    } else {
        // 3D Secure V1 flow
        acsURL = result.getItem().getAuthorizationInformation().getUrl();
        challengeRequest = result.getItem().getAuthorizationInformation().getData();
    }
    // redirect customer to ACS server for 3D Secure authentication
}

/** Request was valid but payment was declined. */
else if (result.isValid()) {
    String declineReason = result.getItem().getError();
    // proceed with declined payment flow
}

/** Request was invalid. 
*   Possible reasons: wrong credentials, unsupported currency, suspended account, etc. 
*/
else {
    CardinityError error = result.getCardinityError();
    // log error details for debugging 
    // proceed with error handling flow
}
```

#### Finalize pending payment

```java
/**
 * paymentId - ID of a pending payment
 * challengeResponse - data received from ACS. cRes in case of 3D Secure V2 flow, paRes in case of 3D Secure V1 flow
 * v2Flow - indicator whether to use 3D Secure V2 flow
 */
Result<Payment> result;
if (v2Flow) {
    result = client.finalizePaymentV2(paymentId, challengeResponse);
} else {
    result = client.finalizePayment(paymentId, challengeResponse);
}

if (result.isValid()) {
    /** Request was valid and payment was approved. */
    if (result.getItem().getStatus() == Payment.Status.APPROVED) {
        UUID paymentId = result.getItem().getId();
        // proceed with successful payment flow
    }    
    /** If finalizePaymentV2() was called and a technical error occured during payment finalization,
      * Cardinity will try to perform 3D Secure V1 authorization. You can either retry 3D Secure V2 flow,
      * or perform 3D Secure V1 flow
      */
    else if (result.getItem().getStatus() == Payment.Status.PENDING) {
        if (result.getItem().isThreedsV1()) {
            // Perform 3D Secure V1 flow
        }
        if (result.getItem().isThreedsV2()) {
            // Retry payment finalization with 3D Secure V2 flow
        }
    }
}

/** Request was valid but payment was declined. */
else if (result.isValid()) {
    String declineReason = result.getItem().getError();
    // proceed with declined payment flow
}

/** Request was invalid. */
else {
    CardinityError error = result.getCardinityError();
    // log error details for debugging 
    // proceed with error handling flow
}
```

#### Create recurring payment

```java
Payment payment = new Payment();
payment.setAmount(new BigDecimal(10));
payment.setCurrency("EUR");
payment.setCountry("LT");
payment.setPaymentMethod(Payment.Method.RECURRING);
Recurring recurringPayment = new Recurring();
// set to ID of a previous payment
recurringPayment.setPaymentId(previousPaymentId);
payment.setPaymentInstrument(recurringPayment);

Result<Payment> result = client.createPayment(payment);
```

#### Get existing payment

```java
Result<Payment> result = client.getPayment(paymentId);
if (result.isValid()) {
    Payment payment = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}        
```

#### Get all payments

```java
Result<List<Payment>> result = client.getPayments();
if (result.isValid()) {
    List<Payment> payments = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

### Refunds [API](https://developers.cardinity.com/api/v1/#refunds)

#### Create new refund

```java
Refund refund = new Refund(new BigDecimal(10));
Result<Refund> result = client.createRefund(paymentId, refund);

/** Request was valid and refund was approved. */
if (result.isValid() && result.getItem().getStatus() == Refund.Status.APPROVED) {
    refund = result.getItem();
    // proceed with successful refund flow
}

/** Request was valid but refund was declined. */
else if (result.isValid()) {
     String declineReason = result.getItem().getError();
     // proceed with declined refund flow
}

/** Request was invalid. */
else {
    CardinityError error = result.getCardinityError();
    // handle error
}
```

#### Get existing refund

```java
Result<Refund> result = client.getRefund(paymentId, refundId);

if (result.isValid()) {
    Refund refund = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}   
```

#### Get all refunds

```java
Result<List<Refund>> result = client.getRefunds(paymentId);

if (result.isValid()) {
    List<Refund> refunds = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

### Settlements [API](https://developers.cardinity.com/api/v1/#settlements)

#### Create new settlement

```java
Settlement settlement = new Settlement(new BigDecimal(10));
Result<Settlement> result = client.createSettlement(paymentId, settlement);

/** Request was valid and settlement was approved. */
if (result.isValid() && result.getItem().getStatus() == Settlement.Status.APPROVED) {
    settlement = result.getItem();
    // proceed with successful settlement flow
}

/** Request was valid but settlement was declined. */
else if (result.isValid()) {
     String declineReason = result.getItem().getError();
     // proceed with declined settlement flow
}

/** Request was invalid. */
else {
    CardinityError error = result.getCardinityError();
    // handle error
}
```

#### Get existing settlement

```java
Result<Settlement> result = client.getSettlement(paymentId, settlementId);

if (result.isValid()) {
    Settlement settlement = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}   
```

#### Get all settlements

```java
Result<List<Settlement>> result = client.getSettlements(paymentId);

if (result.isValid()) {
    List<Settlement> settlements = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

### Voids [API](https://developers.cardinity.com/api/v1/#voids)

#### Create new void

```java
Result<Void> result = client.createVoid(paymentId, new Void());

/** Request was valid and void was approved. */
if (result.isValid() && result.getItem().getStatus() == Void.Status.APPROVED) {
    Void voidPayment = result.getItem();
    // proceed with successful void flow
}

/** Request was valid but void was declined. */
else if (result.isValid()) {
     String declineReason = result.getItem().getError();
     // proceed with declined void flow
}

/** Request was invalid. */
else {
    CardinityError error = result.getCardinityError();
    // handle error
}
```

#### Get existing void

```java
Result<Void> result = client.getVoid(paymentId, voidId);

if (result.isValid()) {
    Void voidPayment = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}   
```

#### Get all voids

```java
Result<List<Void>> result = client.getVoids(paymentId);

if (result.isValid()) {
    List<Void> voids = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

### Chargebacks [API](https://developers.cardinity.com/api/v1/#chargebacks)

#### Get specific chargeback of a payment

```java
Result<Chargeback> result = client.getChargeback(paymentId, chargebackId);

if (result.isValid()) {
    Chargeback chargeback = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

#### Get chargebacks of a payment

```java
Result<List<Chargeback>> result = client.getChargebacks(paymentId);

if (result.isValid()) {
    List<Chargeback> chargebacks = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

#### Get all latest chargebacks

```java
int limit = 10;
Result<List<Chargeback>> result = client.getAllChargebacks(limit);

if (result.isValid()) {
    List<Chargeback> chargebacks = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

### Payment Link API

#### Create a new payment link

```java
PaymentLink paymentLink = new PaymentLink();
paymentLink.setAmount(new BigDecimal("1.00"));
paymentLink.setCurrency("EUR");
paymentLink.setDescription("Product description");

Result<PaymentLink> result = client.createPaymentLink(paymentLink);

if (result.isValid()) {
    PaymentLink paymentLink = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

#### Update a payment link

```java
PaymentLinkUpdate paymentLinkUpdate = new PaymentLinkUpdate();
paymentLinkUpdate.setEnabled(false);

Result<PaymentLink> result = client.updatePaymentLink(existingPaymentLinkId, paymentLinkUpdate);

if (result.isValid()) {
    PaymentLink paymentLink = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

#### Get a payment link

```java
Result<PaymentLink> result = client.getPaymentLink(existingPaymentLinkId);

if (result.isValid()) {
    PaymentLink paymentLink = result.getItem();
}
else {
    CardinityError error = result.getCardinityError();
}
```

## Exceptions

Call of `CardinityClient` methods may result in unchecked exception being thrown. 

Base exception class is `CardinityException`

There can be two types of exceptions thrown:

* `ValidationException` - validation of data passed to method has failed.
* `CardinityClientException` - network errors, internal client errors, etc.
