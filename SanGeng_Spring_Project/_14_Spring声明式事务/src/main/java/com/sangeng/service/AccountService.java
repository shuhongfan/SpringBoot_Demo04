package com.sangeng.service;

public interface AccountService {
    /**
     * 转账
     * @param outId 转出账户的id
     * @param inId 转出账户的id
     * @param money 转账金额
     */
    public void transfer(Integer outId,Integer inId,Double money);

    public void log();
}
