package com.github.zxbu.webdavteambition.config;

import com.github.zxbu.webdavteambition.model.result.TFile;
import com.github.zxbu.webdavteambition.store.AliYunDriverClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AliYunDriverCronTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliYunDriverCronTask.class);

    @Autowired
    private AliYunDriverClientService mAliYunDriverClientService;

    /**
     * 每隔10分钟请求一下接口，保证token不过期
     */
    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 10 * 60 * 1000)
    public void refreshToken() {
        try {
            LOGGER.info("定时刷新 Refresh Token ↓↓↓↓↓");
            TFile root = mAliYunDriverClientService.getTFileByPath("/");
            mAliYunDriverClientService.getTFiles(root.getFile_id());
        } catch (Throwable e) {
            LOGGER.error("", e);
        } finally {
            LOGGER.info("定时刷新 Refresh Token ↑↑↑↑↑");
        }
    }
}
