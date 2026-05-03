package za.ac.cput.patterns.simplefactory;

public class AnomalyAlertNotification extends Notification {

    public AnomalyAlertNotification(String recipientId, String referenceId) {
        super(recipientId, referenceId);
    }

    @Override
    public String getMessage() {
        return "Anomaly detected on meter " + getReferenceId()
               + ". Immediate review required.";
    }

    @Override
    public String getType() {
        return "ANOMALY_ALERT";
    }
}