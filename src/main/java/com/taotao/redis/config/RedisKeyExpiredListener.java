package com.taotao.redis.config;

import com.taotao.redis.mysql.entity.RedPacket;
import com.taotao.redis.mysql.mapper.RedPacketMapper;
import com.taotao.redis.util.AbstractMsgQueueService;
import com.taotao.redis.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private RedPacketMapper redPacketMapper;

    @Autowired
    private AbstractMsgQueueService queueService;

    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        logger.info("key {} expired!", expiredKey);

        String redPacketId = StringUtils.substringAfter(expiredKey, RedisUtil.RED_PACKET_PREFIX);
        if (StringUtils.isNotEmpty(redPacketId)) {
            RedPacket redPacket = redPacketMapper.getRedPacketById(redPacketId);
            if (null != redPacket) {
                queueService.returnRedPacket(redPacket.getUser(), redPacketId);
            }
        }
    }
}
