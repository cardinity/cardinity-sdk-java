package com.cardinity;

import com.cardinity.exceptions.CardinityException;
import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.*;
import com.cardinity.model.Void;
import com.cardinity.oauth.CardinityOAuthProvider;
import com.cardinity.rest.CardinityRestClient;
import com.cardinity.rest.RestClient;
import com.cardinity.rest.RestResource;
import com.cardinity.rest.RestResource.RequestMethod;
import com.cardinity.rest.URLUtils;
import com.cardinity.validators.*;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CardinityClient {

    private final RestClient restClient;

    private final static TypeToken<Payment> PAYMENT_TYPE = new TypeToken<Payment>() {
    };
    private final static TypeToken<List<Payment>> PAYMENT_LIST_TYPE = new TypeToken<List<Payment>>() {
    };
    private final static TypeToken<Refund> REFUND_TYPE = new TypeToken<Refund>() {
    };
    private final static TypeToken<List<Refund>> REFUND_LIST_TYPE = new TypeToken<List<Refund>>() {
    };
    private final static TypeToken<Settlement> SETTLEMENT_TYPE = new TypeToken<Settlement>() {
    };
    private final static TypeToken<List<Settlement>> SETTLEMENT_LIST_TYPE = new TypeToken<List<Settlement>>() {
    };
    private final static TypeToken<Void> VOID_TYPE = new TypeToken<Void>() {
    };
    private final static TypeToken<List<Void>> VOID_LIST_TYPE = new TypeToken<List<Void>>() {
    };

    private final static Validator<Payment> paymentValidator = new PaymentValidator();
    private final static Validator<Refund> refundValidator = new RefundValidator();
    private final static Validator<Settlement> settlementValidator = new SettlementValidator();
    private final static Validator<Void> voidValidator = new VoidValidator();

    /**
     * Constructs a CardinityClient object.
     *
     * @param consumerKey    live or testing consumer key provided by cardinity
     * @param consumerSecret live or testing consumer secret provided by cardinity
     */
    public CardinityClient(String consumerKey, String consumerSecret) {
        if (ValidationUtils.isBlank(consumerKey) || ValidationUtils.isBlank(consumerSecret))
            throw new ValidationException("Consumer key and consumer secret must be not null");
        this.restClient = new CardinityRestClient(new CardinityOAuthProvider(consumerKey, consumerSecret));
    }

    /**
     * Creates a new payment.
     *
     * @param payment Payment object.
     * @return a Result wrapper containing either a result Payment object or a CardinityError object.
     * @throws ValidationException if payment object contains validation errors.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Payment> createPayment(Payment payment) {

        paymentValidator.validate(payment);
        return restClient.sendRequest(RequestMethod.POST, URLUtils.buildUrl(), PAYMENT_TYPE, payment);
    }

    /**
     * Finalizes a pending payment.
     *
     * @param paymentId     id of a payment to be finalized.
     * @param authorizeData PaRes data received from ACS server.
     * @return a Result wrapper containing either a result Payment object or a CardinityError object.
     * @throws ValidationException if paymentId is null or authorizeDate is empty.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Payment> finalizePayment(UUID paymentId, String authorizeData) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        if (ValidationUtils.isBlank(authorizeData)) throw new ValidationException("authorizeData is mandatory.");

        Payment finalizePayment = new Payment();
        finalizePayment.setAuthorizeData(authorizeData);

        return restClient.sendRequest(RequestMethod.PATCH, URLUtils.buildUrl(paymentId), PAYMENT_TYPE, finalizePayment);
    }

    /**
     * Finds and returns a payment specified by a paymentId.
     *
     * @param paymentId id of a payment to be found.
     * @return a Result wrapper containing either a found Payment object or a CardinityError object.
     * @throws ValidationException if paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Payment> getPayment(UUID paymentId) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId), PAYMENT_TYPE);
    }

    /**
     * Returns a list of last 10 payments.
     *
     * @return a Result wrapper containing either a list of Payment objects or a CardinityError object.
     * @throws CardinityException if internal client error occurs.
     */
    public Result<List<Payment>> getPayments() {
        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(), PAYMENT_LIST_TYPE);
    }

    /**
     * Returns a list of last payments size of which is specified by a limit parameter.
     *
     * @param limit number of last payment to be returned. Value must be between 1 and 100.
     * @return a Result wrapper containing either a list of Payment objects or a CardinityError object.
     * @throws ValidationException if limit is less than 1 or larger than 100.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<List<Payment>> getPayments(int limit) {

        if (!ValidationUtils.validateInteger(limit, 1, 100))
            throw new ValidationException("Limit must be a positive value between 1 and 100");

        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(), PAYMENT_LIST_TYPE, params);
    }

    /**
     * Refunds an approved payment.
     *
     * @param paymentId id of a payment to be refunded.
     * @param refund    Refund object containing amount to be refunded.
     * @return a Result wrapper containing either a result Refund object or a CardinityError object.
     * @throws ValidationException if refund object contains validation errors or paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Refund> createRefund(UUID paymentId, Refund refund) {

        if (paymentId == null) throw new ValidationException("paymentId must be not null.");

        refundValidator.validate(refund);

        return restClient.sendRequest(RequestMethod.POST, URLUtils.buildUrl(paymentId, RestResource.Resource.REFUNDS)
                , REFUND_TYPE, refund);

    }

    /**
     * Finds and returns a refund specified by a paymentId and a refundId.
     *
     * @param paymentId id of payment which was refunded.
     * @param refundId  id of a refund to be found.
     * @return a Result wrapper containing either a found Refund object or a CardinityError object.
     * @throws ValidationException if paymentId or refundId are null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Refund> getRefund(UUID paymentId, UUID refundId) {

        if (paymentId == null) throw new ValidationException("paymentId must be not null.");

        if (refundId == null) throw new ValidationException("refundId must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource.REFUNDS,
                refundId), REFUND_TYPE);
    }

    /**
     * Returns a list of refunds for a specified payment.
     *
     * @param paymentId id of a payment refunds of which will be returned.
     * @return a Result wrapper containing a list of Refund objects or a CardinityError object.
     * @throws ValidationException if paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<List<Refund>> getRefunds(UUID paymentId) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource.REFUNDS),
                REFUND_LIST_TYPE);
    }

    /**
     * Settles an authorized payment.
     *
     * @param paymentId  id of a payment to be settled.
     * @param settlement Settlement object containing amount to be settled.
     * @return a Result wrapper containing either a result Settlement object or a CardinityError object.
     * @throws ValidationException if settlement object contains validation errors or paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Settlement> createSettlement(UUID paymentId, Settlement settlement) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        settlementValidator.validate(settlement);

        return restClient.sendRequest(RequestMethod.POST, URLUtils.buildUrl(paymentId, RestResource.Resource
                .SETTLEMENTS), SETTLEMENT_TYPE, settlement);
    }

    /**
     * Finds and returns a settlement specified by a paymentId and a settlementId.
     *
     * @param paymentId    id of a payment which was settled.
     * @param settlementId id of settlement to be found.
     * @return a Result wrapper containing either a found Settlement object or a CardinityError object.
     * @throws ValidationException if paymentId or settlementId are null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Settlement> getSettlement(UUID paymentId, UUID settlementId) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        if (settlementId == null) throw new ValidationException("settlementId must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource
                .SETTLEMENTS, settlementId), SETTLEMENT_TYPE);
    }

    /**
     * Returns a list of settlements for a specified payment.
     *
     * @param paymentId id of a payment settlements of which will be returned.
     * @return a Result wrapper containing a list of Settlement objects or a CardinityError object.
     * @throws ValidationException if paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<List<Settlement>> getSettlements(UUID paymentId) {

        if (paymentId == null) throw new ValidationException("paymentId must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource
                .SETTLEMENTS), SETTLEMENT_LIST_TYPE);
    }

    /**
     * Voids an authorized payment.
     *
     * @param paymentId id of a payment to be voided.
     * @param voidP     Void object.
     * @return a Result wrapper containing either a result Void object or a CardinityError object.
     * @throws ValidationException if void object contains validation errors or paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Void> createVoid(UUID paymentId, Void voidP) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        voidValidator.validate(voidP);

        return restClient.sendRequest(RequestMethod.POST, URLUtils.buildUrl(paymentId, RestResource.Resource.VOIDS),
                VOID_TYPE, voidP);
    }

    /**
     * Finds and returns a void specified by a paymentId and a voidId.
     *
     * @param paymentId id of a payment which was voided.
     * @param voidId    id of a void to be found.
     * @return a Result wrapper containing either a found Void object or a CardinityError object.
     * @throws ValidationException if paymentId or voidId are null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<Void> getVoid(UUID paymentId, UUID voidId) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        if (voidId == null) throw new ValidationException("voidId must be not null.");


        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource.VOIDS,
                voidId), VOID_TYPE);
    }

    /**
     * Returns a list of voids for a specified payment.
     *
     * @param paymentId id of a payment voids of which will be returned.
     * @return a Result wrapper containing a list of Void objects or a CardinityError object.
     * @throws ValidationException if paymentId is null.
     * @throws CardinityException  if internal client error occurs.
     */
    public Result<List<Void>> getVoids(UUID paymentId) {

        if (paymentId == null) throw new ValidationException("paymentID must be not null.");

        return restClient.sendRequest(RequestMethod.GET, URLUtils.buildUrl(paymentId, RestResource.Resource.VOIDS),
                VOID_LIST_TYPE);
    }

}
