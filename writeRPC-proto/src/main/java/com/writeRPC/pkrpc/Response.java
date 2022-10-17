package com.writeRPC.pkrpc;

import lombok.Data;

/*表示相应*/
@Data
public class Response {
    private int code;//判断返回是否成功 0-成功
    private String message="OK!";//具体错误信息
    private Object data;//具体返回的数据
}
