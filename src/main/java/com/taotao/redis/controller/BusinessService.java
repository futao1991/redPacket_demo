package com.taotao.redis.controller;

import com.taotao.redis.mysql.entity.Record;
import com.taotao.redis.mysql.entity.RedPacket;
import com.taotao.redis.mysql.mapper.RedPacketMapper;
import com.taotao.redis.mysql.service.AccountService;
import com.taotao.redis.mysql.service.RecordService;
import com.taotao.redis.mysql.service.RedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessService {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private AccountService accountService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private RedPacketService redPacketService;

    @Autowired
    private RedPacketMapper redPacketMapper;

    /**
     * 发红包业务操作
     * @param user         用户id
     * @param redPacketId  红包id
     * @param totalMoney   红包总金额
     * @param number       红包数量
     * @return             true/false
     */
    @Transactional
    public boolean sendRedPacket(String user, String redPacketId, long totalMoney, int number) {

        //账户扣减
        accountService.addOrReduceMoney(user, -totalMoney);

        //红包发送记录
        recordService.addRecord(user, redPacketId, totalMoney, Record.Operation.SEND);

        //创建红包金额记录
        redPacketService.addRedPacket(redPacketId, user, totalMoney, number);

        return true;
    }

    /**
     * 抢红包业务操作
     * @param user         用户id
     * @param redPacketId  红包id
     * @param totalMoney   抢到的金额
     * @return             true/false
     */
    @Transactional
    public boolean gradRedPacket(String user, String redPacketId, long totalMoney) {

        //账户增加
        accountService.addOrReduceMoney(user, totalMoney);

        //红包抢到记录
        recordService.addRecord(user, redPacketId, totalMoney, Record.Operation.GRAB);

        //更新红包金额
        redPacketService.updateRedPacket(redPacketId, totalMoney);

        logger.info(String.format("红包金额%.2f元已入%s账户", totalMoney * 1.0 / 100, user));

        return true;
    }

    /**
     * 返还红包操作
     * @param user         用户id
     * @param redPacketId  红包id
     * @return             true/false
     */
    @Transactional
    public boolean returnRedPacket(String user, String redPacketId) {
        RedPacket redPacket = redPacketMapper.getRedPacketById(redPacketId);
        if (null == redPacket) {
            return false;
        }

        long remainMoney = redPacket.getMoney();

        if (remainMoney > 0) {
            //账户增加
            accountService.addOrReduceMoney(user, remainMoney);

            //红包返还记录
            recordService.addRecord(user, redPacketId, remainMoney, Record.Operation.RETURN);

            logger.info(String.format("红包%s剩余金额%.2f元已返还%s账户", redPacketId, remainMoney * 1.0 / 100, user));
        }

        //删除红包记录
        redPacketMapper.deleteRedPacket(redPacketId);

        return true;
    }
}
