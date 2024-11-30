package utils;

import java.io.Serializable;

public class PeerReviewRequest implements Serializable {
    public String userId;
    public String message;

    public PeerReviewRequest() {} // default constructor (required for Jackson)

    public PeerReviewRequest(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
