package com.taotao.redis.util;

import com.taotao.redis.controller.BusinessService;
import com.taotao.redis.mysql.entity.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class MemoryMsgQueueService extends AbstractMsgQueueService implements InitializingBean {

    private class MsgEvent {

        private String user;

        private String redPacketId;

        private long totalMoney;

        private Record.Operation operation;

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

        public long getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(long totalMoney) {
            this.totalMoney = totalMoney;
        }

        public Record.Operation getOperation() {
            return operation;
        }

        public void setOperation(Record.Operation operation) {
            this.operation = operation;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger("console");

    private Deque<MsgEvent> queue = new LinkedBlockingDeque<>();

    private ExecutorService executors = Executors.newFixedThreadPool(1);

    @Autowired
    private BusinessService businessService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void gradRedPacket(String user, String redPacketId, long totalMoney) {
        MsgEvent event = new MsgEvent();
        event.setUser(user);
        event.setRedPacketId(redPacketId);
        event.setTotalMoney(totalMoney);
        event.setOperation(Record.Operation.GRAB);

        queue.add(event);
    }

    @Override
    public void returnRedPacket(String user, String redPacketId) {
        MsgEvent event = new MsgEvent();
        event.setUser(user);
        event.setRedPacketId(redPacketId);
        event.setOperation(Record.Operation.RETURN);

        queue.add(event);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    MsgEvent event = queue.poll();
                    if (null != event) {
                        try {
                            processMsgEvent(event);
                        } catch (Exception e) {
                            logger.error("process event failed", e);
                        }
                    }
                }
            }
        });
    }

    private void processMsgEvent(MsgEvent event) {
        if (Record.Operation.GRAB == event.getOperation()) {
            businessService.gradRedPacket(event.user, event.redPacketId, event.totalMoney);
        } else if (Record.Operation.RETURN == event.getOperation()) {
            businessService.returnRedPacket(event.user, event.redPacketId);
            redisUtil.deleteRedPacket(event.user, event.redPacketId);
        }
    }
}
