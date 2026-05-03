package za.ac.cput.patterns.simplefactory;

public class ReportReadyNotification extends Notification {

    public ReportReadyNotification(String recipientId, String referenceId) {
        super(recipientId, referenceId);
    }

    @Override
    public String getMessage() {
        return "Your report " + getReferenceId() + " is ready for download.";
    }

    @Override
    public String getType() {
        return "REPORT_READY";
    }
}