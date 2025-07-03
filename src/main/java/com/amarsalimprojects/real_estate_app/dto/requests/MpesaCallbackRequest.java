package com.amarsalimprojects.real_estate_app.dto.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class MpesaCallbackRequest {

    @JsonProperty("Body")
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        @JsonProperty("stkCallback")
        private StkCallback stkCallback;

        public StkCallback getStkCallback() {
            return stkCallback;
        }

        public void setStkCallback(StkCallback stkCallback) {
            this.stkCallback = stkCallback;
        }
    }

    @Data
    public static class StkCallback {

        @JsonProperty("MerchantRequestID")
        private String merchantRequestID;

        @JsonProperty("CheckoutRequestID")
        private String checkoutRequestID;

        @JsonProperty("ResultCode")
        private int resultCode;

        @JsonProperty("ResultDesc")
        private String resultDesc;

        @JsonProperty("CallbackMetadata")
        private CallbackMetadata callbackMetadata;

        // Getters and Setters...
    }

    @Data
    public static class CallbackMetadata {

        @JsonProperty("Item")
        private List<CallbackItem> item;

        public List<CallbackItem> getItems() {
            return item;
        }

        public void setItem(List<CallbackItem> item) {
            this.item = item;
        }
    }

    @Data
    public static class CallbackItem {

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private Object value;

        // Getters and Setters...
    }
}
