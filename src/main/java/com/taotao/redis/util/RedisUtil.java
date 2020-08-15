package com.taotao.redis.util;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("console");

    /*红包前缀*/
    public static final String RED_PACKET_PREFIX = "redPacket-";

    /*红包记录队列*/
    private static final String RED_PACKET_RECORD = "redPacket_record";

    /*红包数量队列*/
    private static final String RED_PACKET_NUMBER = "redPacket_num";

    @Value("${redPacket.expire.time}")
    private long EXPIRE_TIME;

    private static final Long ERROR_CODE = -500L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String luaScript = "";

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream("get_rp.lua");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder script = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            script.append(line).append("\n");
        }
        luaScript = script.toString();
        logger.info("加载lua脚本完毕");
    }

    /**
     * 创建红包记录到redis中
     * @param key     红包id
     * @param money   金额
     * @param number  数量
     * @return
     */
    public Pair<Boolean, String> setRedPacketMoney(String key, long money, int number) {
        try {
            redisTemplate.opsForValue().set(RED_PACKET_PREFIX + key, money, EXPIRE_TIME, TimeUnit.SECONDS);
            redisTemplate.opsForHash().put(RED_PACKET_NUMBER, key, number);
            logger.info("写入队列：key:{}, 金额:{}, 数量:{}", key, money, number);
            return Pair.with(true, null);
        } catch (Exception e) {
            logger.error("set red packet money failed: ", e);
            return Pair.with(false, e.getMessage());
        }
    }

    /**
     * 通过lua脚本抢红包
     * @param userId       用户id
     * @param redPacketId  红包id
     * @return
     */
    public Pair<Long, String> getRedPacket(String userId, String redPacketId) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            script.setScriptText(luaScript);
            Long result = redisTemplate.execute(script, Arrays.asList(userId, redPacketId), new ArrayList<>());
            return Pair.with(result, null);
        } catch (Exception e) {
            logger.error("execute lua script failed: ", e);
            return Pair.with(ERROR_CODE, e.getMessage());
        }
    }

    /**
     * 删除redis中的红包记录
     * @param userId       用户id
     * @param redPacketId  红包id
     * @return
     */
    public Pair<Boolean, String> deleteRedPacket(String userId, String redPacketId) {
        try {
            redisTemplate.opsForHash().delete(RED_PACKET_RECORD, String.format("%s-%s", userId, redPacketId));
            redisTemplate.opsForHash().delete(RED_PACKET_NUMBER, redPacketId);
            return Pair.with(true, null);
        } catch (Exception e) {
            logger.error("delete red packet failed: ", e);
            return Pair.with(false, e.getMessage());
        }
    }
}
