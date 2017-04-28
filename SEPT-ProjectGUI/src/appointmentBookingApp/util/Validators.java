package appointmentBookingApp.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Created by David on 11/04/2017.
 */
public class Validators {

    static String email = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    static String password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
    static String phone = "\\d{8,10}";

    public static boolean validate(String input, String type){
        String regex = null;

        if(type.equals("email")){
            regex = email;
        }
        else if(type.equals("password")){
            regex = password;
        }
        else if(type.equals("phone")){
            regex = phone;
        }

        Pattern pattern =
                Pattern.compile(regex);

        Matcher matcher =
                pattern.matcher(input);

        return matcher.find();
    }

}
