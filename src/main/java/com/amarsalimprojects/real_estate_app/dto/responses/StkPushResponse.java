package com.amarsalimprojects.real_estate_app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StkPushResponse {

    private String merchantRequestId;
    private String checkoutRequestId;
    private String responseCode;
    private String responseDescription;
    private String customerMessage;
    private boolean success;
    private String errorMessage;

    public static StkPushResponse success(String merchantRequestId, String checkoutRequestId,
            String responseCode, String responseDescription, String customerMessage) {
        return StkPushResponse.builder()
                .merchantRequestId(merchantRequestId)
                .checkoutRequestId(checkoutRequestId)
                .responseCode(responseCode)
                .responseDescription(responseDescription)
                .customerMessage(customerMessage)
                .success(true)
                .build();
    }

    public static StkPushResponse failure(String errorMessage) {
        return StkPushResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}
