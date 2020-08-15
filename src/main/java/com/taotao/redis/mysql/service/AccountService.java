package com.taotao.redis.mysql.service;

import com.taotao.redis.mysql.entity.Account;
import com.taotao.redis.mysql.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 增加或者减少金钱
     * @param userId 用户id
     * @param money  增加或减少的金钱
     */
    public void addOrReduceMoney(String userId, Long money) {
        accountMapper.addMoney(userId, money);
    }
}
