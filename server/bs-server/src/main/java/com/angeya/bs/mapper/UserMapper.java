package com.angeya.bs.mapper;

import com.angeya.bs.model.StorageSpace;
import com.angeya.bs.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteById(Integer id);

    int insertSelective(User user);

    User selectById(Integer id);

    User selectByMailAndPwd(User user);

    List<User> selectAll();

    int selectUserNumByMail(String name);

    StorageSpace selectStorageSpace(Integer id);

    int updateById(User user);
}