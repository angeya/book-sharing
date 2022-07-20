package com.angeya.bs.service;

import com.angeya.bs.config.FileConfig;
import com.angeya.bs.consts.Const;
import com.angeya.bs.enums.OperateTypeEnum;
import com.angeya.bs.enums.SimpleResultEnum;
import com.angeya.bs.enums.VerifyCodeEnum;
import com.angeya.bs.mapper.UserMapper;
import com.angeya.bs.model.OperateLog;
import com.angeya.bs.model.StorageSpace;
import com.angeya.bs.model.User;
import com.angeya.bs.result.ContentResult;
import com.angeya.bs.result.SimpleResult;
import com.angeya.bs.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:
 * @Author: Angeya
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogManager logManager;

    @Autowired
    private VerifyCodeManager verifyCodeManager;

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    public SimpleResult createUser(User user) {
        VerifyCodeEnum validateResult = this.verifyCodeManager.validateVerifyCode(user.getMail(), user.getVerifyCode());
        if (!VerifyCodeEnum.CORRECT.equals(validateResult)) {
            return new SimpleResult(validateResult.getCode(), validateResult.getMsg());
        }
        if (isUserNameExist(user.getMail())) {
            return new SimpleResult(SimpleResultEnum.USER_NAME_EXIST);
        }
        // 对明文密码进行加密
        String cryptPassword = MD5.getMD5String(user.getPassword());
        user.setPassword(cryptPassword);
        int result = userMapper.insertSelective(user);
        if (result == 1) {
            return new SimpleResult(SimpleResultEnum.SUCCESS);
        }
        return new SimpleResult(SimpleResultEnum.FAILED);

    }

    /**
     * 用户登录
     *
     * @param user 用户信息
     * @return 是否成功
     */
    public ContentResult<User> login(HttpSession session, User user) {
        // 对明文密码进行加密
        String cryptPassword = MD5.getMD5String(user.getPassword());
        user.setPassword(cryptPassword);
        User dbUser = userMapper.selectByMailAndPwd(user);
        if (dbUser != null) {
            session.setAttribute(Const.SESSION_KEY, dbUser);
            OperateLog operateLog = new OperateLog(dbUser.getId(), OperateTypeEnum.LOGIN.getIndex());
            logManager.log(operateLog);
            return new ContentResult<>(SimpleResultEnum.SUCCESS, dbUser);
        }
        return new ContentResult<>(SimpleResultEnum.FAILED, null);
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public ContentResult<List<User>> getAllUser() {
        List<User> userList = userMapper.selectAll();
        return new ContentResult<>(SimpleResultEnum.SUCCESS, userList);
    }

    /**
     * 通过 id 获取用户
     *
     * @param id 用户 id
     * @return 查询结果
     */
    public ContentResult<User> getUserById(Integer id) {
        User user = userMapper.selectById(id);
        return new ContentResult<>(SimpleResultEnum.SUCCESS, user);
    }

    /**
     * 通过 id 删除用户
     * @param id 用户 id
     * @return 删除结果
     */
    public SimpleResult deleteUserById(Integer id) {
        int result = userMapper.deleteById(id);
        if (result == 1) {
            return new SimpleResult(SimpleResultEnum.SUCCESS);
        }
        return new SimpleResult(SimpleResultEnum.FAILED);
    }

    public ContentResult<StorageSpace> getStorageInfo(HttpSession session) {
        User user = (User)session.getAttribute(Const.SESSION_KEY);
        StorageSpace storageSpace = userMapper.selectStorageSpace(user.getId());
        return new ContentResult<>(SimpleResultEnum.SUCCESS, storageSpace);
    }

    /**
     * 判断用户名是否已经存在
     *
     * @param name 用户名
     * @return 是否存在
     */
    public boolean isUserNameExist(String name) {
        int userNumber = userMapper.selectUserNumByMail(name);
        if (userNumber == 0) {
            return false;
        }
        if (userNumber > 1) {
            logger.warn("There are more than one user has the same mail");
        }
        return true;
    }
}
