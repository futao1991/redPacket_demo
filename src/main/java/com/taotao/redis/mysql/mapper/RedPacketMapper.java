package com.taotao.redis.mysql.mapper;

import com.taotao.redis.mysql.entity.RedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RedPacketMapper {

    int addRedPacket(@Param("redPacket") RedPacket redPacket);

    int updateRedPacket(@Param("redPacketId") String redPacketId, @Param("money") Long money);

    RedPacket getRedPacketById(@Param("redPacketId") String redPacketId);

    int deleteRedPacket(@Param("redPacketId") String redPacketId);
}
