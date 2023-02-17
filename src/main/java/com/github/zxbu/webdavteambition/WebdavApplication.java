package com.github.zxbu.webdavteambition;

import com.github.zxbu.webdavteambition.config.AliYunDriveProperties;
import com.github.zxbu.webdavteambition.filter.ErrorFilter;
import com.github.zxbu.webdavteambition.model.*;
import com.github.zxbu.webdavteambition.model.result.CreateFileResult;
import com.github.zxbu.webdavteambition.model.result.TFile;
import com.github.zxbu.webdavteambition.model.result.TFileListResult;
import com.github.zxbu.webdavteambition.model.result.UploadPreResult;
import com.github.zxbu.webdavteambition.store.AliYunDriverFileSystemStore;
import net.sf.webdav.WebdavServlet;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedHashMap;
import java.util.Map;

@EnableScheduling
@SpringBootApplication
@RegisterReflectionForBinding({
        CreateFileResult.class,
        TFile.class,
        TFileListResult.class,
        UploadPreResult.class,
        CreateFileRequest.class,
        DownloadRequest.class,
        FileGetRequest.class,
        FileListRequest.class,
        FileType.class,
        MoveRequest.class,
        MoveRequestId.class,
        Page.class,
        PathInfo.class,
        RefreshUploadUrlRequest.class,
        RemoveRequest.class,
        RenameRequest.class,
        UploadFinalRequest.class,
        UploadPreInfo.class,
        UploadPreRequest.class,
        AliYunDriveProperties.class,
})
public class WebdavApplication {
    public static void main(String[] args) {
         SpringApplication.run(WebdavApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean<WebdavServlet> myServlet(){
        ServletRegistrationBean<WebdavServlet> servletRegistrationBean = new ServletRegistrationBean<>(new WebdavServlet(), "/*");
        Map<String, String> inits = new LinkedHashMap<>();
        inits.put("ResourceHandlerImplementation", AliYunDriverFileSystemStore.class.getName());
        // inits.put("ResourceHandlerImplementation", LocalFileSystemStore.class.getName());
        inits.put("rootpath", "./");
        inits.put("storeDebug", "1");
        servletRegistrationBean.setInitParameters(inits);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ErrorFilter());
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }
}
