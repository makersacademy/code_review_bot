package utils;

import java.io.Serializable;

public class CodeSubmission implements Serializable {
    public String userId;
    public String userName;
    public String code;

    public CodeSubmission() {} // default constructor (required for Jackson)

    public CodeSubmission(String userId, String userName, String code) {
        this.userId = userId;
        this.userName = userName;
        this.code = code;
    }
}
