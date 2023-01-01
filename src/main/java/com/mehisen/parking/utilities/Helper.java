package com.mehisen.parking.utilities;

import com.mehisen.parking.payload.EmailDetails;

import java.util.Random;

public class Helper {

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    public static String getEmailBody(String token){
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>The code below to change your password:</p>"
                + "<h2>\"" + token + "\"</h2>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        return content;
    }
}
