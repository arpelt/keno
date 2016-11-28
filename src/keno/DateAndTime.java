/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Date and time manipulation methods.
 *
 * @author AP
 */
public class DateAndTime {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*String date to Epoch time */
    public static long dateToEpoch(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        long dateEpochTimeInMillis = localDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        return dateEpochTimeInMillis;
    }

    /*Add or subtract day(s) */
    public static LocalDateTime dayPlusDay(LocalDateTime time, int days) {
        LocalDateTime timePlusDays = time.plusDays(days);
        return timePlusDays;
    }

    /*Set date-time time to zero */
    public static LocalDateTime setTime(LocalDateTime time) {
        LocalDateTime timeHoursMinsSecsZero = time.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return timeHoursMinsSecsZero;
    }

    /*Convert a string date-time to LocalDateTime */
    public static LocalDateTime stringToTime(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);
        return dateTime;
    }

    /*Duration between days  */
    public static long durationInDays(LocalDateTime startDate, LocalDateTime endDate) {
        long daysBetween = Duration.between(startDate.toInstant(ZoneOffset.ofTotalSeconds(0)), endDate.toInstant(ZoneOffset.ofTotalSeconds(0))).toDays();
        return daysBetween;
    }

    /*Date-time to Epoch time  */
    public static long dateTimeToEpochTime(LocalDateTime time) {
        long dateEpochTimeInMillis = time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        return dateEpochTimeInMillis;
    }

    /*Read last draw date from results file */
    public static LocalDateTime lastDrawFromFile() {
        String[] lastDrawIdAndDate = Keno.lastDraw();
        LocalDateTime startDate = DateAndTime.stringToTime(lastDrawIdAndDate[1]);
        return startDate;
    }

    /*String date-time to Epoch time, returns startDate in epoch time*/
    public static long startDateEpochTimeMillis(LocalDateTime startDate) {
        LocalDateTime startDateTimeToZero = DateAndTime.setTime(startDate);
        long startDateEpochTimeInMillis = dateTimeToEpochTime(startDateTimeToZero);
        return startDateEpochTimeInMillis;
    }

    /*Current date-time to string */
    //public static void timeToString() {
    //    LocalDateTime timeNow = LocalDateTime.now();
    //    String LocalDateAndTime = timeNow.format(dateTimeFormatter);
    //}
}
