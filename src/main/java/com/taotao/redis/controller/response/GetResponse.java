package com.taotao.redis.controller.response;

public class GetResponse {

    private Long money;

    private String message;

    private String errMsg;

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static GetResponse createResponse(String errMsg) {
        GetResponse response = new GetResponse();
        response.setErrMsg(errMsg);
        return response;
    }

    public static GetResponse createResponse(Long money, String message) {
        GetResponse response = new GetResponse();
        response.setMoney(money);
        response.setMessage(message);
        return response;
    }
}
