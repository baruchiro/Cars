package rothkoff.baruch.cars;

import java.util.Calendar;

public class B {
    public static Customer customer;

    /**
     * <p>
     *     this Method get two Calendar objects and check if his date (31/12/2016) is compering
     * </p>
     * @param oneCal Calendar Object
     * @param twoCal Calendar object
     * @return 'true' if Day, Month, Year is ==. else false.
     */
    public static boolean CompareWithYearMonthDay(Calendar oneCal, Calendar twoCal) {
        if (oneCal.get(Calendar.DAY_OF_MONTH) == twoCal.get(Calendar.DAY_OF_MONTH)) {
            if (oneCal.get(Calendar.MONTH) == twoCal.get(Calendar.MONTH)) {
                if (oneCal.get(Calendar.YEAR) == twoCal.get(Calendar.YEAR))
                    return true;
            }
            return false;
        }
        return false;
    }

    public static Calendar getCalenderWithOnlyDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getLongWithOnlyDate(long date){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return getCalenderWithOnlyDate(c).getTimeInMillis();
    }

    public static boolean isBetweenDate(long dateStart,long dateEnd,long temp) {
        if (temp >= dateStart && temp <= dateEnd) return true;
        return false;
    }

    public static final class Keys {
        public static final String CARS = "cars";
        public static final String CUSTOMERS ="customers";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME ="lastName";
        public static final String DATE_OF_BIRTH ="dateOfBirth";
        public static final String UID = "uid";
        public static final String ID_NUMBER = "IDnumber";
        public static final String CAR_NUMBER = "carNumber";
        public static final String BRAND = "brand";
        public static final String COLOR = "color";
        public static final String IS_YOUNG = "isYoung";
        public static final String SIZE = "size";
        public static final String PARK_LOCATION = "parkLocation";
        public static final String TARIFF_UID = "tariffUid";
        public static final String CUSTOMER = "CUSTOMER";

        public static final String TARIFFS = "tariffs";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String SEAT_COUNT = "seatCount";
        public static final String ENGINE_CAPACITY = "engineCapacity";
        public static final String YOUNG_PRICE = "youngPrice";
        public static final String RENTS = "rents";
    }

    public class Constants {
        public static final String mainPreference="rothkoff.baruch.cars.MAIN_PREFERENCE";
        public static final String FIRST_LAUNCH = "rothkoff.baruch.cars.MAIN_PREFERENCE.FIRST_LAUNCH";
        public static final String ANY_FRAGMENT = "rothkoff.baruch.cars.ANY_FRAGMENT";
        public static final long YEAR_IN_MILISECONDS = 31556952000L;
        public static final long DAY_IN_MILISECONDS = 86400000L;
        public static final java.lang.String FRAGMENT_TITLE = "rothkoff.baruch.cars.FRAGMENT_TITLE";
    }
}
