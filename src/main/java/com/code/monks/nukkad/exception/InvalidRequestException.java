package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class InvalidRequestException  extends RuntimeException{

    private final ResponseErrorCodes error;

   public InvalidRequestException(ResponseErrorCodes error){
       super(error.getMessage());
       this.error = error;
   }
    public InvalidRequestException(ResponseErrorCodes error, String customMessage) {
        super(customMessage);  // ← uses our fullErrorReport as the message
        this.error = error;
    }
}
