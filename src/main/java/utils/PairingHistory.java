package utils;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class PairingHistory {
    public static String filePath = "pairing_history.csv";

    public static ArrayList<Pairing> getHistory(String userId) {
        ArrayList<Pairing> pairings = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath);
                CSVParser csvParser = CSVFormat.Builder.create()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)) {

            for (CSVRecord record : csvParser) {
                String studentA = record.get("studentA");
                String studentB = record.get("studentB");
                String channelName = record.get("channelName");
                Date datetime = toDate(record.get("datetime"));

                if (studentA.equals(userId) || studentB.equals(userId)) {
                    pairings.add(new Pairing(studentA, studentB, channelName, datetime));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return pairings;
    }

    private static Date toDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return formatter.parse(dateString);
    }
}
