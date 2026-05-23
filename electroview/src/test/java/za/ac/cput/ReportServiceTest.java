package za.ac.cput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.Report;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.ReportRepository;
import za.ac.cput.service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    private Report testReport;

    @BeforeEach
    void setUp() {
        testReport = Report.builder()
                .requestedBy("analyst-1")
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 31))
                .format(ReportFormat.CSV)
                .build();
    }

    @Test
void requestReport_InlineRange_SetsReadyStatus() {
    when(reportRepository.save(any(Report.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Report result = reportService.requestReport(
            "analyst-1",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 31),
            ReportFormat.CSV,
            null, null
    );

    assertEquals(ReportStatus.RReady, result.getStatus());
    assertNotNull(result.getFilePath());
}

   @Test
void requestReport_LargeRange_SetsQueuedStatus() {
    when(reportRepository.save(any(Report.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Report result = reportService.requestReport(
            "analyst-1",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 6, 1),
            ReportFormat.PDF,
            null, null
    );

    assertEquals(ReportStatus.QUEUED, result.getStatus());
}

    @Test
    void requestReport_InvalidDateRange_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                reportService.requestReport(
                        "analyst-1",
                        LocalDate.of(2026, 6, 1),
                        LocalDate.of(2026, 1, 1),
                        ReportFormat.CSV,
                        null, null
                )
        );
    }

    @Test
    void findById_ReportExists_ReturnsReport() {
        when(reportRepository.findById("report-1"))
                .thenReturn(Optional.of(testReport));

        Report result = reportService.findById("report-1");

        assertNotNull(result);
        assertEquals(ReportFormat.CSV, result.getFormat());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(reportRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                reportService.findById("bad-id")
        );
    }

    @Test
    void findByUser_ReturnsUserReports() {
        when(reportRepository.findByRequestedBy("analyst-1"))
                .thenReturn(List.of(testReport));

        List<Report> result = reportService.findByUser("analyst-1");

        assertEquals(1, result.size());
        assertEquals("analyst-1", result.get(0).getRequestedBy());
    }
}