package com.taotao.redis.mysql.mapper;

import com.taotao.redis.mysql.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecordMapper {

    int addRecord(@Param("record") Record record);
}
