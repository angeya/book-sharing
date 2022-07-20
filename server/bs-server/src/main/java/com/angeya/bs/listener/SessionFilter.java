package com.angeya.bs.listener;

import com.angeya.bs.consts.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "sessionFilter")
public class SessionFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do Nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //  设置头部，跨域等（最好明确的写允许的域名）
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        String ip = req.getRemoteAddr();
        String uri = req.getRequestURI();

        if ((uri.contains("file") || uri.contains("user"))
                && session.getAttribute(Const.SESSION_KEY) == null
                && !(Const.LOGIN_URI.equals(uri) || Const.CREATE_USER_URI.equals(uri)
                || Const.GET_VERIF_CODE_URI.equals(uri))) {
            // 如果没有登录，且不进行登录，注册，则直接返回
            return;
        }
        logger.info("request status:{}, url:{}, ip:{}, sessionId:{}", res.getStatus(), uri, ip, sessionId);
        // 将参数继续传递下去
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //Do Nothing
    }
}
