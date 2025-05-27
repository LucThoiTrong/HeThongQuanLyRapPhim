package hcmute.edu.vn.HeThongQuanLyRapPhim.observer;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.AppEvent;

public interface Subject {
    void subscribe(Class<? extends AppEvent> eventType, EventListener listener);
    void unsubscribe(Class<? extends AppEvent> eventType, EventListener listener);
    void notify(AppEvent event);
    void shutdown();
}
