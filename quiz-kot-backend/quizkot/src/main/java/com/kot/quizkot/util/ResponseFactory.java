package com.kot.quizkot.util;

import com.kot.quizkot.dto.response.ApiResponse;

public class ResponseFactory {
    
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setSuccess(true);
        res.setStatus(200);
        res.setMessage(message);
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        ApiResponse<T> res = new ApiResponse<>();
        res.setSuccess(false);
        res.setStatus(status);
        res.setMessage(message);
        return res;
    }
}
