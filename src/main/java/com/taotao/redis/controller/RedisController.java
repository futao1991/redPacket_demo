package com.taotao.redis.controller;

import com.taotao.redis.controller.response.CreateResponse;
import com.taotao.redis.controller.response.GetResponse;
import com.taotao.redis.mysql.entity.Account;
import com.taotao.redis.mysql.mapper.AccountMapper;
import com.taotao.redis.mysql.service.AccountService;
import com.taotao.redis.util.AbstractMsgQueueService;
import com.taotao.redis.util.RedisUtil;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class RedisController {

    /*红包不存在*/
    private static final Long RED_PACKET_NOT_EXIST = -2L;

    /*已经抢过红包*/
    private static final Long RED_PACKET_HAS_GET = -1L;

    /*未抢到红包*/
    private static final Long RED_PACKET_NOT_GET = 0L;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AbstractMsgQueueService queueService;

    /**
     * 用户发红包接口
     * @param userId      用户id
     * @param totalMoney  红包总金额(分)
     * @param number      红包数量
     * @return 结果
     */
    @RequestMapping("/redpacket/create")
    @ResponseBody
    public CreateResponse createRedPacket(String userId, long totalMoney, int number) {
        Account account = accountMapper.getAccountById(userId);
        if (null == account) {
            return CreateResponse.createResponse("账户不存在!");
        }

        if (account.getMoney() < totalMoney) {
            return CreateResponse.createResponse("账户余额不足!");
        }

        String uuid = UUID.randomUUID().toString();
        if (businessService.sendRedPacket(userId, uuid, totalMoney, number)) {
            Pair<Boolean, String> pair = redisUtil.setRedPacketMoney(uuid, totalMoney, number);
            if (pair.getValue0()) {
                String message = String.format("用户%s发了%.2f元红包, 共%d个, 红包id:%s",
                        account.getName(), totalMoney * 1.0 / 100, number, uuid);
                return CreateResponse.createResponse(uuid, message);
            } else {
                return CreateResponse.createResponse("发红包出错:" + pair.getValue1());
            }
        } else {
            return CreateResponse.createResponse("内部逻辑错误");
        }
    }

    /**
     * 用户抢红包
     * @param userId         用户id
     * @param redPacketId    红包id
     * @return 结果
     */
    @RequestMapping("/redpacket/get")
    @ResponseBody
    public GetResponse getRedPacket(String userId, String redPacketId) {
        Account account = accountMapper.getAccountById(userId);
        if (null == account) {
            return GetResponse.createResponse("账户不存在!");
        }

        Pair<Long, String> pair = redisUtil.getRedPacket(userId, redPacketId);
        if (null != pair.getValue1()) {
            return GetResponse.createResponse("抢红包出错:" + pair.getValue1());
        } else {
            Long money = pair.getValue0();
            if (RED_PACKET_NOT_EXIST.equals(money)) {
                return GetResponse.createResponse("红包不存在或已过期");
            } else if (RED_PACKET_HAS_GET.equals(money)) {
                return GetResponse.createResponse("已经抢过该红包");
            } else if (RED_PACKET_NOT_GET.equals(money)) {
                return GetResponse.createResponse("红包已抢完");
            } else {
                String message = String.format("用户%s抢到了红包，%.2f元", account.getName(), money * 1.0 /100);
                queueService.gradRedPacket(userId, redPacketId, money);
                return GetResponse.createResponse(money, message);
            }
        }
    }
}
