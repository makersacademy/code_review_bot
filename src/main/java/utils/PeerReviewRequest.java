package utils;

import java.io.Serializable;

public class PeerReviewRequest implements Serializable {
    public String userId;

    public PeerReviewRequest() {} // default constructor (required for Jackson)

    public PeerReviewRequest(String userId) {
        this.userId = userId;
    }
}
