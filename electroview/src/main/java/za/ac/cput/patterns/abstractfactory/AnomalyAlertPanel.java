package za.ac.cput.patterns.abstractfactory;

public class AnomalyAlertPanel implements AlertPanel {

    @Override
    public String render() {
        return "Rendering anomaly alert panel — Open and Escalated anomalies";
    }

    @Override
    public int getMaxAlerts() {
        return 50;
    }
}