package com.crm.crm.util;


import com.crm.crm.dto.response.APIResponseDTO;

public class APIResponseUtil {

    public static <T> APIResponseDTO<T> successResponse(T data) {
        return new APIResponseDTO<>(ResponseConstant.SUCCESS_STATUS, ResponseConstant.HTTP_OK, ResponseConstant.SUCCESS_MESSAGE,data);
    }

    public static <T> APIResponseDTO<T> successResponse(String message, T data) {
        return new APIResponseDTO<>(ResponseConstant.SUCCESS_STATUS, ResponseConstant.HTTP_OK, message, data);
    }

    public static <T> APIResponseDTO<T> successResponse(String message) {
        return new APIResponseDTO<>(ResponseConstant.SUCCESS_STATUS, ResponseConstant.HTTP_OK, message, null);
    }

    public static APIResponseDTO<String> createSuccessResponse(String message) {
        return new APIResponseDTO<>(ResponseConstant.SUCCESS_STATUS, ResponseConstant.HTTP_OK, message, null);
    }

    public static APIResponseDTO<String> createFailureResponse(String message) {
        return new APIResponseDTO<>(ResponseConstant.ERROR_STATUS, ResponseConstant.HTTP_BAD_REQUEST, message, null);
    }

    public static APIResponseDTO<String> createErrorResponse(String errorMessage) {return new APIResponseDTO<>(ResponseConstant.ERROR_STATUS, ResponseConstant.HTTP_BAD_REQUEST, errorMessage, null);
    }

    public static Object errorResponse(String errorMessage) {
        return new APIResponseDTO<>(ResponseConstant.ERROR_STATUS, ResponseConstant.HTTP_BAD_REQUEST, errorMessage, null);
    }
}
