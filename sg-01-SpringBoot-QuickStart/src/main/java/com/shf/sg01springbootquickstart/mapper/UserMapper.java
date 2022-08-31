package com.shf.sg01springbootquickstart.mapper;

import com.shf.sg01springbootquickstart.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    public List<User> findAll();
}
