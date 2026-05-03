package za.ac.cput.patterns.abstractfactory;

public class PersonalUsageChart implements ConsumptionChart {

    @Override
    public String render() {
        return "Rendering personal usage bar chart with billing period comparison";
    }

    @Override
    public String getChartType() {
        return "PERSONAL_USAGE_BAR_CHART";
    }
}