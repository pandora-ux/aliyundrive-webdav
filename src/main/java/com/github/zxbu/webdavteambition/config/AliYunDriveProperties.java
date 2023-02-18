package com.github.zxbu.webdavteambition.config;

import com.github.zxbu.webdavteambition.util.JsonUtil;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@ConfigurationProperties("aliyundrive")
public class AliYunDriveProperties implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliYunDriveProperties.class);
    private static final String META_FILE_NAME = "meta.json";
    public String url = "https://api.aliyundrive.com/v2";
    public transient String authorization = "";
    public String refreshToken;
    public String refreshTokenNext;
    public String workDir = "/workspace/etc/aliyun-driver/";
    public String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36";
    public String driveId;
    public String userId;
    public String deviceId;
    public String appId = "5dde4e1bdf9e4966b387ba58f4b3fdc3";
    public Session session = new Session();

    public Auth auth = new Auth();

    public void save() {
        String json = JsonUtil.toJsonPretty(this);
        File metaFile = new File(workDir, META_FILE_NAME);
        if (!metaFile.exists()) {
            metaFile.getParentFile().mkdirs();
        }
        try {
            FileUtils.write(metaFile, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Error: write meta file failed", e);
        }
    }

    public static AliYunDriveProperties load(String workDir) {
        File metaFile = new File(workDir, META_FILE_NAME);
        if (!metaFile.exists()) {
            return new AliYunDriveProperties();
        }
        try {
            String json = FileUtils.readFileToString(metaFile, StandardCharsets.UTF_8);
            return JsonUtil.readValue(json, AliYunDriveProperties.class);
        } catch (IOException e) {
            LOGGER.error("Error: read meta file failed", e);
        }
        return new AliYunDriveProperties();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String refreshToken = this.refreshToken;
        AliYunDriveProperties other = load(workDir);
        BeanUtils.copyProperties(other, this);
        this.authorization = null;
        if (StringUtils.isEmpty(this.deviceId)) {
            this.deviceId = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
        }
        this.refreshTokenNext = refreshToken;
        if (StringUtils.isEmpty(this.auth.userName)) {
            this.auth.userName = "admin";
        }
        if (StringUtils.isEmpty(this.auth.password)) {
            this.auth.password = "admin";
        }
        save();
    }

    @Data
    public static class Session {
        public String privateKey;
        public String publicKey;
        public String signature;
        public long expireTimeSec = 0;
        public int nonce = 0;

        public boolean isEmpty() {
            return StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(publicKey);
        }

        public boolean isExpired() {
            return expireTimeSec < System.currentTimeMillis() / 1000;
        }
    }

    @Data
    public static class Auth {
        public Boolean enable = true;
        public String userName;
        public String password;
    }
}
