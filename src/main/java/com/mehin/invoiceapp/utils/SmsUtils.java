package com.mehin.invoiceapp.utils;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

import static com.twilio.rest.api.v2010.account.Message.creator;

public class SmsUtils {
    public static final String FROM_NUMBER = "+18883926852";
    public static final String SID_KEY = "AC587f82a7469525ca4c2aa92c393fd1db";
    public static final String TOKEN_KEY = "14e162647721b1f2078be6e34161db6f";

    public static void sendSMS(String to, String messageBody){
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }

}
