package za.ac.cput;

import org.junit.jupiter.api.Test;
import za.ac.cput.patterns.simplefactory.Notification;
import za.ac.cput.patterns.simplefactory.NotificationFactory;
import za.ac.cput.patterns.simplefactory.NotificationFactory.NotificationType;

import static org.junit.jupiter.api.Assertions.*;

class NotificationFactoryTest {

    @Test
    void createAnomalyAlert_ReturnsCorrectType() {
        Notification n = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-42");

        assertEquals("ANOMALY_ALERT", n.getType());
    }

    @Test
    void createAnomalyAlert_MessageContainsMeterReference() {
        Notification n = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-42");

        assertTrue(n.getMessage().contains("meter-42"));
    }

    @Test
    void createBudgetWarning_ReturnsCorrectType() {
        Notification n = NotificationFactory.create(
                NotificationType.BUDGET_WARNING, "consumer-1", "budget-1");

        assertEquals("BUDGET_WARNING", n.getType());
    }

    @Test
    void createBudgetWarning_MessageContainsPercentage() {
        Notification n = NotificationFactory.create(
                NotificationType.BUDGET_WARNING, "consumer-1", "budget-1");

        assertTrue(n.getMessage().contains("80%"));
    }

    @Test
    void createReportReady_ReturnsCorrectType() {
        Notification n = NotificationFactory.create(
                NotificationType.REPORT_READY, "analyst-1", "report-99");

        assertEquals("REPORT_READY", n.getType());
    }

    @Test
    void createReportReady_MessageContainsReportReference() {
        Notification n = NotificationFactory.create(
                NotificationType.REPORT_READY, "analyst-1", "report-99");

        assertTrue(n.getMessage().contains("report-99"));
    }

    @Test
    void newNotification_IsNotRead() {
        Notification n = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-1");

        assertFalse(n.isRead());
    }

    @Test
    void markAsRead_UpdatesReadState() {
        Notification n = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-1");
        n.markAsRead();

        assertTrue(n.isRead());
    }

    @Test
    void twoNotifications_HaveDifferentIds() {
        Notification a = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-1");
        Notification b = NotificationFactory.create(
                NotificationType.ANOMALY_ALERT, "user-1", "meter-1");

        assertNotEquals(a.getNotificationId(), b.getNotificationId());
    }
}