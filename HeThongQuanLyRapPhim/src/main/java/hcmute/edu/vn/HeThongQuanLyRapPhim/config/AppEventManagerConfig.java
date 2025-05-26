package hcmute.edu.vn.HeThongQuanLyRapPhim.config;

import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.AppEventManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppEventManagerConfig {

    @Bean
    @Scope("singleton")
    public AppEventManager appEventManager() {
        return new AppEventManager();
    }
}