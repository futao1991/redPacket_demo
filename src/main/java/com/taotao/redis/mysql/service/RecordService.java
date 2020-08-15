package com.taotao.redis.mysql.service;

import com.taotao.redis.mysql.entity.Record;
import com.taotao.redis.mysql.entity.Record.Operation;
import com.taotao.redis.mysql.mapper.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RecordService {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private RecordMapper recordMapper;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public boolean addRecord(String user, String redPacketId, Long money, Operation operation) {
        Record record = new Record();
        record.setUser(user);
        record.setRedPacketId(redPacketId);
        record.setMoney(money);
        record.setOperation(operation.getOperation());
        record.setTime(getCurrentTime());

        return recordMapper.addRecord(record) > 0;
    }
}
