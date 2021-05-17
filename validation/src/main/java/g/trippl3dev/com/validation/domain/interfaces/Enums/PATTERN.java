package g.trippl3dev.com.validation.domain.interfaces.Enums;



import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mahmoud Abdelaal on 2/20/2018.
 * Common Patterns Class
 */
@StringDef({PATTERN.EMAIL, PATTERN.PHONE, PATTERN.PHONEHOME
        , PATTERN.PASSWORD
        , PATTERN.arabicChars
        , PATTERN.USERNAME
        , PATTERN.DESCRIPTION
        , PATTERN.ARABICDESCRIPTION
        , PATTERN.BIRTHDATE
        , PATTERN.ARABICUSERNAME
        , PATTERN.NUMBERS
        , PATTERN.ANYPATTERN
        , PATTERN.USERNAME
        , PATTERN.PRICES
})
@Retention(RetentionPolicy.SOURCE)
public @interface PATTERN {
    String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    //    String PHONE = "^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{8})$";
    String PHONE = "^(5)([0-9]{8,12})$";
    //    String PHONE ="5.{9}$";
    String PHONEHOME = "^(01)([0-9]{7,20})$";
    //    String PASSWORD = "(?=.*[A-Z]).{6,15}";
//    String PASSWORD  = "(.|\\n){6,20}$";
    String PASSWORD = "(.|\\n){3,20}$";
    //    String PASSWORD = "^[0-9a-zA-Z\u0621-\u064A\u0660-\u0669](?!.*\\.\\.)[a-zA-Z\u0621-\u064A\u0660-\u0669.\\d\\s](?=.*?[A-Z]){5,15}$";
//    String PASSWORD = "(?=.*\\d)      #   must contains one digit from 0-9\n" +
//        "  (?=.*[a-z])   #   must contains one lowercase characters\n" +
//        "  (?=.*[A-Z])   #   must contains one uppercase characters\n" +
//        "  (?=.*[@#$%])   #   must contains one special symbols in the list \"@#$%\"\n" +
//        "              . #   match anything with previous condition checking\n" +
//        "  {6,20}";
    String arabicChars = "\u0621-\u064A\u0660-\u0669";
    String USERNAME = "^[a-zA-Z\u0621-\u064A\u0660-\u0669](?!.*\\.\\.)[a-zA-Z\u0621-\u064A\u0660-\u0669.\\d\\s]{4,19}$";
    String DESCRIPTION = "(.|\\n){15,1000}$";//"^[a-zA-Z\u0621-\u064A\u0660-\u06690-9](?!.*\\.\\.)[a-zA-Z\u0621-\u064A\u0660-\u0669.\\d\\s]{3,140}$";
    String BIRTHDATE = "^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$";
    String ARABICUSERNAME = "(^[\u0621-\u064A\u0660-\u06690-9\\s]{3,30}$)";
    String ARABICDESCRIPTION = "^[\u0621-\u064A\u0660-\u06690-9](?!.*\\.\\.)[\u0621-\u064A\u0660-\u0669.\\d\\s]{3,140}$";
    String NUMBERS = "[0-9\\.]{1,50}$";
    String ANYPATTERN = "(.|\\n){3,1000}$";
    String PRICES = "[1-9][0-9]*";
    String LICENSENUMBER = "(.|\\n){1,14}";
    String CREDITNUMBER = "(.|\\n){14,16}";
    String IDNUMBER = "(.|\\n){10,11}";
    String Timer = "^[0-9][0-9]:[0-9][0-9]$";
}