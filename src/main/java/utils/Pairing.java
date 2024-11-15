package utils;

import java.util.Date;

public class Pairing {
    public String studentA;
    public String studentB;
    public String channelName;
    public Date datetime;

    public Pairing(String studentA, String studentB, String channelName, Date datetime) {
        this.studentA = studentA;
        this.studentB = studentB;
        this.channelName = channelName;
        this.datetime = datetime;
    }

    public String stringFor(String student) {
        String pair = student.equals(studentA) ? studentB : student.equals(studentB) ? studentA : "unknown student";
        return String.format("Paired with %s in channel %s on %s", pair, channelName, datetime);
    }
}
