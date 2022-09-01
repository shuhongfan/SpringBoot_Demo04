package com.sangeng.dao;

import org.apache.ibatis.annotations.Param;

public interface AccoutDao {

    void updateMoney(@Param("id") Integer id,@Param("updateMoney") Double updateMoney);
}
