package com.taotao.redis.controller.response;

public class CreateResponse {

    private String redPacketId;

    private String message;

    private String errMsg;

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
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

    public static CreateResponse createResponse(String errMsg) {
        CreateResponse response = new CreateResponse();
        response.setErrMsg(errMsg);
        return response;
    }

    public static CreateResponse createResponse(String redPacketId, String message) {
        CreateResponse response = new CreateResponse();
        response.setRedPacketId(redPacketId);
        response.setMessage(message);
        return response;
    }
}
