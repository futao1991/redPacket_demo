package com.taotao.redis;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedPacketAPITest {

    private static String postUrl = "http://localhost:9006/redpacket/create";

    private static String getUrl = "http://localhost:9006/redpacket/get";

    private static Map<String, String> nameMap = new HashMap<>();

    static {
        nameMap.put("1498a273-de00-11ea-ada7-18dbf224530e", "Lily");
        nameMap.put("1d727e44-de00-11ea-ada7-18dbf224530e", "Dave");
        nameMap.put("22930dda-de00-11ea-ada7-18dbf224530e", "Lina");
        nameMap.put("28f66900-de00-11ea-ada7-18dbf224530e", "Daisy");
        nameMap.put("300663d5-de00-11ea-ada7-18dbf224530e", "Lemo");
        nameMap.put("41af3633-de00-11ea-ada7-18dbf224530e", "Tim");
        nameMap.put("5dcf56bc-de00-11ea-ada7-18dbf224530e", "John");
        nameMap.put("65c5a189-de00-11ea-ada7-18dbf224530e", "Jack");
        nameMap.put("6fcc67c4-de00-11ea-ada7-18dbf224530e", "Bob");
    }

    private static void gradRedPacket(String userId, String redPacketId) throws Exception {
        String url = String.format("%s?userId=%s&redPacketId=%s", getUrl, userId, redPacketId);
        String getResult = HttpUtils.httpGet(url);
        if (StringUtils.isNotEmpty(getResult)) {
            JSONObject object = JSONObject.parseObject(getResult);
            String errMsg = object.getString("errMsg");
            String name = nameMap.get(userId);
            if (StringUtils.isNotEmpty(errMsg)) {
                System.out.println(String.format("用户%s未抢到红包: %s", name, errMsg));
            } else {
                long money = object.getLong("money");
                System.out.println(String.format("用户%s抢到了红包，%.2f元", name, money * 1.0 /100));
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("userId", "d0669aeb-dd7b-11ea-b332-18dbf224530e");
        map.put("totalMoney", "1000");
        map.put("number", "5");
        String postResult = HttpUtils.httpPost(postUrl, map);

        if (StringUtils.isNotEmpty(postResult)) {
            JSONObject object = JSONObject.parseObject(postResult);
            String redPacketId = object.getString("redPacketId");

            if (null != redPacketId) {
                System.out.println("用户Tom发了红包，金额10元，共5个");

                ExecutorService executor = Executors.newCachedThreadPool();
                for (String user : nameMap.keySet()) {
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                gradRedPacket(user, redPacketId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                executor.shutdown();
            }
        }
    }
}
