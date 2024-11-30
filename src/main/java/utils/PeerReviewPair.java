package utils;

import java.util.Date;

public class PeerReviewPair {
    public String studentA;
    public String studentB;
    public String channelName;
    public Date datetime;

    public PeerReviewPair(String studentA, String studentB, String channelName, Date datetime) {
        this.studentA = studentA;
        this.studentB = studentB;
        this.channelName = channelName;
        this.datetime = datetime;
    }

    public String stringFor(String studentId) {
        String pair = studentId.equals(studentA) ? studentB : studentId.equals(studentB) ? studentA : "unknown student";
        return String.format("Paired with <@%s> in channel *%s* on *%s*", pair, channelName, datetime);
    }
}
