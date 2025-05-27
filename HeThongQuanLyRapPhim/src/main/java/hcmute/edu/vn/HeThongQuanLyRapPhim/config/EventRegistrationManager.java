package hcmute.edu.vn.HeThongQuanLyRapPhim.config;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.InvoiceGeneratedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.PasswordRecoveryRequestedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.event.RegistrationInitiatedEvent;
import hcmute.edu.vn.HeThongQuanLyRapPhim.listener.PasswordRecoveryEmailListener;
import hcmute.edu.vn.HeThongQuanLyRapPhim.listener.PaymentEmailListener;
import hcmute.edu.vn.HeThongQuanLyRapPhim.listener.RegistrationEmailListener;
import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.AppEventManager;
import hcmute.edu.vn.HeThongQuanLyRapPhim.observer.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.List;

@Component
public class EventRegistrationManager {

    private final AppEventManager appEventManager;

    private final List<EventListener> allRegisteredListeners;


    @Autowired
    public EventRegistrationManager(AppEventManager appEventManager,
                                    List<EventListener> allRegisteredListeners) {
        this.appEventManager = appEventManager;
        this.allRegisteredListeners = allRegisteredListeners;
    }

    @PostConstruct
    public void registerEventListeners() {
        for (EventListener listener : allRegisteredListeners) {
            if (listener instanceof PasswordRecoveryEmailListener) {
                appEventManager.subscribe(PasswordRecoveryRequestedEvent.class, listener);
                System.out.println("Đã đăng ký PasswordRecoveryEmailListener cho PasswordRecoveryRequestedEvent.");
            } else if (listener instanceof PaymentEmailListener) {
                appEventManager.subscribe(InvoiceGeneratedEvent.class, listener);
                System.out.println("Đã đăng ký PaymentEmailListener cho InvoiceGeneratedEvent.");
            } else if (listener instanceof RegistrationEmailListener) {
                appEventManager.subscribe(RegistrationInitiatedEvent.class, listener);
                System.out.println("Đã đăng ký RegistrationEmailListener cho RegistrationInitiatedEvent.");
            }
        }
    }

    @PreDestroy
    public void onShutdown() {
        System.out.println("EventRegistrationManager: Thực hiện shutdown AppEventManager...");
        if (appEventManager != null) {
            appEventManager.shutdown();
        }
    }
}