package hcmute.edu.vn.HeThongQuanLyRapPhim.observer;

import hcmute.edu.vn.HeThongQuanLyRapPhim.event.AppEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class AppEventManager implements Subject {
//  1 sự kiện -> 1 list xử lý sự kiện
    private final Map<Class<? extends AppEvent>, List<EventListener>> listenersPerEventType = new HashMap<>();

//  Biến để xử lý đa luồng
    private final ExecutorService notificationExecutor;

    public AppEventManager() {
        this.notificationExecutor = Executors.newCachedThreadPool();
    }

    @Override
    public void subscribe(Class<? extends AppEvent> eventType, EventListener listener) {
//      computeIfAbsent: Nếu đã có -> dùng lại danh sách cũ, ngược lại tạo mới 1 danh sách
        List<EventListener> listeners = listenersPerEventType.computeIfAbsent(eventType, k -> new ArrayList<>());
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unsubscribe(Class<? extends AppEvent> eventType, EventListener listener) {
        List<EventListener> listeners = listenersPerEventType.get(eventType);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listenersPerEventType.remove(eventType);
            }
        }
    }

    @Override
    public void notify(AppEvent event) {
        if (event == null) {
            return;
        }
//      Lấy loại sự kiện
        Class<? extends AppEvent> eventType = event.getClass();
//      Lấy danh sách xử lý sự kiện từ loại sự kiện đó
        List<EventListener> specificListeners = listenersPerEventType.get(eventType);

        if (specificListeners != null && !specificListeners.isEmpty()) {
            for (EventListener listener : specificListeners) {
                notificationExecutor.submit(() -> {
                    try {
                        listener.update(event);
                    } catch (Exception e) {
                        System.err.println("Lỗi khi listener " + listener.getClass().getName() + " xử lý sự kiện " + eventType.getName() + ": " + e.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void shutdown() {
        notificationExecutor.shutdown();
        try {
            if (!notificationExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                notificationExecutor.shutdownNow();
                if (!notificationExecutor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("AppEventManager: ExecutorService (notificationExecutor) không dừng hoàn toàn.");
            }
        } catch (InterruptedException ie) {
            notificationExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}