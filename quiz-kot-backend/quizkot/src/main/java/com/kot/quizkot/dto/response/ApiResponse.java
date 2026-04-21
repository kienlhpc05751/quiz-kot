package com.kot.quizkot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {
   private boolean success;
    private String message;
    private int status;
    private T data;
    private Object error;
    private long timestamp = System.currentTimeMillis();;
    
      // 
}
