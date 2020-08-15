package com.taotao.redis.util;


public abstract class AbstractMsgQueueService {

    /**
     * 发送抢红包消息到队列
     * @param user          用户id
     * @param redPacketId   红包id
     * @param totalMoney    红包总金额
     */
    public abstract void gradRedPacket(String user, String redPacketId, long totalMoney);

    /**
     * 发送退还红包消息到队列
     * @param user          用户id
     * @param redPacketId   红包id
     */
    public abstract void returnRedPacket(String user, String redPacketId);
}
