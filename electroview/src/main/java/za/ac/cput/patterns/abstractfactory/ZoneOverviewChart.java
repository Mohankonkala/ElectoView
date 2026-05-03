package za.ac.cput.patterns.abstractfactory;

public class ZoneOverviewChart implements ConsumptionChart {

    @Override
    public String render() {
        return "Rendering zone overview line chart with 5-minute refresh";
    }

    @Override
    public String getChartType() {
        return "ZONE_OVERVIEW_LINE_CHART";
    }
}