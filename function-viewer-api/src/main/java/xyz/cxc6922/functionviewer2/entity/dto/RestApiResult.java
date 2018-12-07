package xyz.cxc6922.functionviewer2.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiResult {
    private String msg;
    private Object info;
    private int code;

    public RestApiResult(Object info) {
        this.info = info;
    }
}
