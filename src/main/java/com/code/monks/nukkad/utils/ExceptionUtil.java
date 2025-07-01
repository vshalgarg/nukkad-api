package com.code.monks.nukkad.utils;

public class ExceptionUtil {

    public static String extractFieldConflictMessage(String message){
        if(message == null) return "Data conflict occured.";

        if(message.contains("Duplicate entry")){
            if(message.contains("gst_in")) return "This gst_in is already exists";
            if(message.contains("address_line1")) return "This address is already exists";
            if(message.contains(" mobile_number")) return "This mobile_number is already exists";
            if(message.contains("store_qr_id")) return "This store_qr_id is already exists";
            if(message.contains("email_id")) return "This email_id is already exists";
        }
        return message;
    }
}
