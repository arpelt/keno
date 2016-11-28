/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;

import static keno.DateAndTime.dateTimeFormatter;

/**
 * Reads JSON object and array structures using javax.json API.
 * @author AP
 */
public class ReadJsonData {

    public static void jsonData(String draw) {
        String resultFile2 = "keno_result_file2.txt";
        JsonReader reader = Json.createReader(new StringReader(draw));
        JsonObject drawObject = reader.readObject();
        reader.close();
        JsonArray drawsArray = drawObject.getJsonArray("draws");
        for (int i = 0; i < drawsArray.size(); i++) {
            JsonObject draws = drawsArray.getJsonObject(i);

            JsonNumber closeTimeEpochTime = draws.getJsonNumber("closeTime");
            long longCloseTimeEpoch = closeTimeEpochTime.longValue();

            LocalDateTime closeTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(longCloseTimeEpoch), ZoneId.systemDefault());
            
            String closeTimeFormatted = closeTime.format(dateTimeFormatter);

            //JsonNumber drawTime = draws.getJsonNumber("drawTime");
            
            JsonObject results = drawsArray.getJsonObject(i);

            JsonArray primaryAndSecondaryArray = results.getJsonArray("results");

            JsonObject primary = primaryAndSecondaryArray.getJsonObject(0);

            JsonArray primaryNumbersArray = primary.getJsonArray("primary");

            JsonArray secondaryNumberArray = primary.getJsonArray("secondary");
            
            String drawData = (draws.getString("id") + ", " + draws.getString("brandName") + " " + closeTimeFormatted + ", " + "[{\"primary\":" + primaryNumbersArray + ",\"secondary\":" + secondaryNumberArray + "}]");

            //System.out.println(drawData);

            Writer.writeToFileString(drawData, resultFile2);
        }
    }

}
