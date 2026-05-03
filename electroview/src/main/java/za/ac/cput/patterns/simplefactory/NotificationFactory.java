package za.ac.cput.patterns.simplefactory;

public class NotificationFactory {

    public enum NotificationType {
        ANOMALY_ALERT,
        BUDGET_WARNING,
        REPORT_READY
    }

    public static Notification create(NotificationType type,
                                      String recipientId,
                                      String referenceId) {
        return switch (type) {
            case ANOMALY_ALERT  -> new AnomalyAlertNotification(recipientId, referenceId);
            case BUDGET_WARNING -> new BudgetWarningNotification(recipientId, referenceId);
            case REPORT_READY   -> new ReportReadyNotification(recipientId, referenceId);
        };
    }
}