package appointmentBookingApp.util;


public class Util {
    /**
     * Method that takes a string and puts it to title case.
     * @param str String
     * @return String
     */
    public static String toTitleCase(String str){
        if (str.isEmpty())
        {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
