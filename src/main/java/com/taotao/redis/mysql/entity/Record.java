package com.taotao.redis.mysql.entity;

import java.io.Serializable;

public class Record implements Serializable {

    public enum Operation {

        SEND("send"),

        GRAB("grab"),

        RETURN("return");

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }
    }

    private Integer id;

    private String user;

    private String redPacketId;

    private Long money;

    private String operation;

    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
