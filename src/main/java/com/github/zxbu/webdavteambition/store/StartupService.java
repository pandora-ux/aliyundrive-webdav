package com.github.zxbu.webdavteambition.store;

import com.github.zxbu.webdavteambition.config.AliYunDriverCronTask;
import com.github.zxbu.webdavteambition.manager.AliYunSessionManager;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupService {

    private AliYunSessionManager mAliYunSessionManager;

    @Autowired
    private AliYunDriverClientService mAliYunDriverClientService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        startAliYunSessionManager();
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
        stopAliYunSessionManager();
    }

    private void startAliYunSessionManager(){
        AliYunSessionManager mgr = mAliYunSessionManager;
        if (mgr != null) {
            mgr.stop();
        }
        mgr = new AliYunSessionManager(mAliYunDriverClientService.client);
        mAliYunSessionManager = mgr;
        mgr.start();
    }

    private void stopAliYunSessionManager(){
        AliYunSessionManager mgr = mAliYunSessionManager;
        if (mgr != null) {
            mgr.stop();
            mAliYunSessionManager = null;
        }
    }
}
