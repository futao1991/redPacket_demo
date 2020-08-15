package com.taotao.redis.mysql.service;

import com.taotao.redis.mysql.entity.RedPacket;
import com.taotao.redis.mysql.mapper.RedPacketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedPacketService {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private RedPacketMapper redPacketMapper;

    public boolean addRedPacket(String id, String user, Long money, int number) {
        RedPacket redPacket = new RedPacket();
        redPacket.setId(id);
        redPacket.setUser(user);
        redPacket.setMoney(money);
        redPacket.setNumber(number);

        return redPacketMapper.addRedPacket(redPacket) > 0;
    }

    public boolean updateRedPacket(String redPacketId, Long money) {
        return redPacketMapper.updateRedPacket(redPacketId, money) > 0;
    }
}
