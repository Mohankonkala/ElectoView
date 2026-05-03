package za.ac.cput.patterns.simplefactory;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class Notification {

    private final String notificationId;
    private final String recipientId;
    private final String referenceId;
    private boolean read;
    private final LocalDateTime createdAt;

    public Notification(String recipientId, String referenceId) {
        this.notificationId = UUID.randomUUID().toString();
        this.recipientId = recipientId;
        this.referenceId = referenceId;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public abstract String getMessage();
    public abstract String getType();

    public void markAsRead() {
        this.read = true;
    }
}