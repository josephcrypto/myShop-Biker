package cn.coding.com.myshop.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse {

    private int status;

    private String message;

    private long timeStamp;

    public CustomErrorResponse() {
        super();
    }

    public CustomErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
