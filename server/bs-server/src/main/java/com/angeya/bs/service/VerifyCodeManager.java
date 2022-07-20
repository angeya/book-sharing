package com.angeya.bs.service;

import com.angeya.bs.config.MailConfig;
import com.angeya.bs.consts.Const;
import com.angeya.bs.enums.VerifyCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Desc:
 * @Author: Angeya
 * DateTime: 2021-09-22 21:19
 */
@Service
@EnableScheduling
public class VerifyCodeManager {
    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeManager.class);

    private final ConcurrentMap<String, VerifyCode> mailCodeMap = new ConcurrentHashMap<>();

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    MailConfig mailConfig;
    @Autowired
    private ThreadPoolTaskExecutor mailSendExecutor;

    @Async(value = "mailSendExecutor")
    public void sendMail(String mail) {
        String code = this.createCode();
        VerifyCode verifyCode = new VerifyCode(code, System.currentTimeMillis());
        this.mailCodeMap.put(mail, verifyCode);
        //构建并发送邮件
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailConfig.getUsername());
            simpleMailMessage.setTo(mail);
            simpleMailMessage.setSubject(Const.MAIL_SUBJECT);
            simpleMailMessage.setText(String.format(Const.REGISTER_MAIL_TEMPLATE, code));
            mailSender.send(simpleMailMessage);
            logger.info("send mail to {}, code is {}", mail, code);
        } catch (Exception e) {
            logger.warn("send mail to {} failed", mail, e);
        }
    }

    /**
     * 创建验证码 （1位随机数 + 3位时间戳）
     * @return 四位验证码
     */
    private String createCode() {
        String currentTimeStr = Long.toString(System.currentTimeMillis());
        return new Random().nextInt(9) + currentTimeStr.substring(currentTimeStr.length() - Const.VERIFICATION_CODE_LENGTH + 1);
    }

    /**
     * 检验验证码
     * @param mail 邮箱
     * @param code 验证码
     * @return 验证结果
     */
    public VerifyCodeEnum validateVerifyCode(String mail, String code){
        if (this.mailCodeMap.containsKey(mail)){
            if (this.mailCodeMap.get(mail).getCode().equals(code)) {
                return VerifyCodeEnum.CORRECT;
            }
            return VerifyCodeEnum.ERROR;
        }
        return VerifyCodeEnum.INVALID;
    }

    /**
     * 清除过期的验证码, 20s 一次
     */
    @Scheduled(fixedDelay = 20L * 1000)
    public void clearOverdueVerifyCode() {
        if (this.mailCodeMap.isEmpty()) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, VerifyCode> item : this.mailCodeMap.entrySet()) {
            long createTime = item.getValue().getCreateTime();
            if (currentTime - createTime > 180L * 1000) {
                this.mailCodeMap.remove(item.getKey());
                logger.info("remove overdue verifyCode: {}", item);
            }
        }
    }

    /**
     * 验证码类，（实现Delayed接口，放入延迟队列）
     */
    private static class VerifyCode {
        private final String code;
        private final long createTime;

        public VerifyCode(String code, long createTime) {
            this.code = code;
            this.createTime = createTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "VerifyCode{" +
                    "code='" + code + '\'' +
                    ", createTime=" + createTime +
                    '}';
        }
    }
}
