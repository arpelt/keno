/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

import static java.lang.Thread.sleep;

/**
 * Send an HTTP GET request to JSON API. The successful response contains one
 * day draw results (one to three draws). Request URI has two variables
 * "startDateEpochTimeInMillis" and "endDateEpochTimeInMillis":
 * "startDateEpochTimeInMillis" is the last draw date which is saved in the
 * results file. "endDateEpochTimeInMillis" is "startDateEpochTimeInMillis" plus
 * 1 day. The variable "days" defines the time in days between the last draw
 * date and current date plus one day. A maximum HTTP GET requests is 100 per
 * update.
 *
 * @author AP
 */
public class HttpConnection {

    public static void sendHttpRequest() {
        String host = "https://www.veikkaus.fi";
        String path = "";
        String path1 = "/api/v1/draw-games/draws?game-names=KENO&status=RESULTS_AVAILABLE&date-from=";
        String param = "&date-to=";
        String url = "";
        String draws = "";
        URL requestUrl = null;
        HttpURLConnection connection = null;

        LocalDateTime endDate = DateAndTime.dayPlusDay(LocalDateTime.now(), 1);
        endDate = DateAndTime.setTime(endDate);
        LocalDateTime startDate = DateAndTime.lastDrawFromFile();
        LocalDateTime drawDate = startDate;
        LocalDateTime tmpNextDrawDate = startDate;
        long startDateEpochTimeInMillis = DateAndTime.startDateEpochTimeMillis(startDate);
        tmpNextDrawDate = DateAndTime.setTime(tmpNextDrawDate);
        long endDateEpochTimeInMillis = DateAndTime.dateTimeToEpochTime(tmpNextDrawDate);

        long days = DateAndTime.durationInDays(drawDate, endDate);
        if (days > 100) {
            days = 100;
        }

        for (long i = 0; i <= days; i++) {
            try {
                startDateEpochTimeInMillis = endDateEpochTimeInMillis;
                tmpNextDrawDate = DateAndTime.dayPlusDay(tmpNextDrawDate, 1);

                tmpNextDrawDate = DateAndTime.setTime(tmpNextDrawDate);
                endDateEpochTimeInMillis = DateAndTime.dateTimeToEpochTime(tmpNextDrawDate);

                path = path1 + startDateEpochTimeInMillis + param + endDateEpochTimeInMillis;
                url = host + path;

                try {
                    requestUrl = new URL(url);
                } catch (MalformedURLException ex) {
                    System.err.println("MalformedURLException: " + ex);
                    KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                    KenoForm.statusBar.setText("Update failed");
                }
                try {
                    connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("X-ESA-APi-Key", "ROBOT");
                } catch (IOException ex) {
                    System.err.println("HttpURLConnection: " + ex);
                    KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                    KenoForm.statusBar.setText("Update failed");
                }
                try {
                    if (connection.getResponseCode() == 200) {
                        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        while ((draws = input.readLine()) != null) {
                            response.append(draws);
                            ReadJsonData.jsonData(draws);
                        }
                        input.close();
                    } else {
                        KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                        KenoForm.statusBar.setText("Update failed");
                        break;
                    }
                } catch (IOException ex) {
                    System.err.println("IOException: " + ex);
                    KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                    KenoForm.statusBar.setText("Update failed");
                    break;
                }
                sleep(2000);
            } catch (InterruptedException ex) {
                System.err.println("InterruptedException: " + ex);
                KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                KenoForm.statusBar.setText("Update failed");
            }
        }
    }

}
