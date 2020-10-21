package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Address;
import com.cardinity.model.BrowserInfo;
import com.cardinity.model.Threeds2Data;

import static com.cardinity.validators.ValidationUtils.*;

public class Threeds2DataValidator implements Validator<Threeds2Data> {

    @Override
    public void validate(Threeds2Data threeds2Data) {
        if (threeds2Data == null) {
            throw new ValidationException("Missing threeds2Data object");
        }
        if (isBlank(threeds2Data.getNotificationUrl())) {
            throw new ValidationException("Notification URL is mandatory");
        }
        if (threeds2Data.getBrowserInfo() == null) {
            throw new ValidationException("Browser info is mandatory");
        }
        validate(threeds2Data.getBrowserInfo());

        if (threeds2Data.getBillingAddress() != null) {
            validate(threeds2Data.getBillingAddress());
        }
        if (threeds2Data.getDeliveryAddress() != null) {
            validate(threeds2Data.getDeliveryAddress());
        }
    }

    private void validate(Address address) {
        if (!validateIntervalLength(address.getAddressLine1(), 1, 50)) {
            throw new ValidationException("Address line 1 is mandatory. Maximum length is 50 characters");
        }
        if (!isBlank(address.getAddressLine2()) && !validateIntervalLength(address.getAddressLine2(), 1, 50)) {
            throw new ValidationException("Address line 2 maximum length is 50 characters");
        }
        if (!isBlank(address.getAddressLine3()) && !validateIntervalLength(address.getAddressLine3(), 1, 50)) {
            throw new ValidationException("Address line 3 maximum length is 50 characters");
        }
        if (!validateIntervalLength(address.getCity(), 1, 50)) {
            throw new ValidationException("City is mandatory. Maximum length is 50 characters");
        }
        if (!validateExactLength(address.getCountry(), 2)) {
            throw new ValidationException("Country is mandatory. Must be a ISO 3166-1 alpha-2 country code.");
        }
        if (!validateIntervalLength(address.getPostalCode(), 1, 16)) {
            throw new ValidationException("Postal code is mandatory. Maximum length is 16 characters");
        }
    }

    private void validate(BrowserInfo browserInfo) {
        if (isBlank(browserInfo.getAcceptHeader())) {
            throw new ValidationException("Accept header info is mandatory");
        }
        if (isBlank(browserInfo.getBrowserLanguage())) {
            throw new ValidationException("Browser language is mandatory");
        }
        if (browserInfo.getScreenWidth() == null) {
            throw new ValidationException("Screen width is mandatory");
        }
        if (browserInfo.getScreenHeight() == null) {
            throw new ValidationException("Screen height is mandatory");
        }
        if (browserInfo.getChallengeWindowSize() == null) {
            throw new ValidationException("Challenge window size is mandatory");
        }
        if (isBlank(browserInfo.getUserAgent())) {
            throw new ValidationException("User agent is mandatory");
        }
        if (browserInfo.getColorDepth() == null) {
            throw new ValidationException("Color depth is mandatory");
        }
        if (browserInfo.getTimeZone() == null) {
            throw new ValidationException("Time zone is mandatory");
        }
    }
}
