package za.ac.cput.patterns.factorymethod;

public class PdfReportExporter extends ReportExporter {

    @Override
    protected ExportedFile createFile(String reportId, String data) {
        String content = "[PDF HEADER]\nReport ID: "
                         + reportId + "\n\n" + data + "\n[PDF FOOTER]";
        return new ExportedFile(
                reportId + "_report.pdf",
                content,
                "application/pdf"
        );
    }
}