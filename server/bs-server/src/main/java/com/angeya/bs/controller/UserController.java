package com.angeya.bs.controller;

import com.angeya.bs.consts.Const;
import com.angeya.bs.enums.SimpleResultEnum;
import com.angeya.bs.model.StorageSpace;
import com.angeya.bs.model.User;
import com.angeya.bs.result.ContentResult;
import com.angeya.bs.result.SimpleResult;
import com.angeya.bs.service.VerifyCodeManager;
import com.angeya.bs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 *
 * @Author: Angeya
 * @date: 2021/8/5 10:20
 */

@RestController
@RequestMapping("user")
@CrossOrigin
@Api(value = "UserManager", description = "用户管理")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private VerifyCodeManager verifyCodeManager;

    @GetMapping("getAllUser")
    public ContentResult getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("createUser")
    public SimpleResult getFilesByRelativePath(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("login")
//    @ApiOperation(value = "登录", httpMethod = "POST", response = ModelMap.class, notes = "login")
    public ContentResult<User> login(HttpServletRequest request, @ApiParam("用户实体") @RequestBody User user) {
        return userService.login(request.getSession(), user);
    }

    @GetMapping("isLogin")
    public SimpleResult isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(Const.SESSION_KEY) != null){
            return new SimpleResult(SimpleResultEnum.SUCCESS);
        }
        return new SimpleResult(SimpleResultEnum.FAILED);
    }

    @GetMapping("logout")
    public SimpleResult login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(Const.SESSION_KEY, null);
        return new SimpleResult(SimpleResultEnum.SUCCESS);
    }

    @GetMapping("getStorageInfo")
    public ContentResult<StorageSpace> getStorageInfo(HttpServletRequest request) {
        return userService.getStorageInfo(request.getSession());
    }

    @PostMapping("getVerifyCode")
    public SimpleResult getVerificationCode(@RequestBody String mail) {
        mail = mail.replace(Const.MAIL_ENCODE_SYMBOL, Const.AT).replace("=", "");
        this.verifyCodeManager.sendMail(mail);
        return new SimpleResult(SimpleResultEnum.SUCCESS);
    }
}
