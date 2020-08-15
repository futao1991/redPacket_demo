package com.taotao.redis.mysql.mapper;

import com.taotao.redis.mysql.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    int addMoney(@Param("userId")String userId, @Param("money")Long money);

    Account getAccountById(@Param("userId")String userId);
}
