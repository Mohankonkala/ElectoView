package za.ac.cput.patterns.factorymethod;

public class CsvReportExporter extends ReportExporter {

    private static final String CSV_HEADER =
            "meter_id,zone,date,kwh_consumed,anomaly_flag\n";

    @Override
    protected ExportedFile createFile(String reportId, String data) {
        String content = CSV_HEADER + data;
        return new ExportedFile(
                reportId + "_report.csv",
                content,
                "text/csv"
        );
    }
}