package com.angeya.bs.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
    private final Logger logger = LoggerFactory.getLogger(AppInitListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("context destroyed");
    }
}
