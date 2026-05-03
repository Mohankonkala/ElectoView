package za.ac.cput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.AnomalyRepository;
import za.ac.cput.service.AnomalyService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnomalyServiceTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    @InjectMocks
    private AnomalyService anomalyService;

    private Anomaly testAnomaly;

    @BeforeEach
    void setUp() {
        testAnomaly = Anomaly.builder()
                .meterId("meter-1")
                .readingId("reading-1")
                .thresholdAtTime(180.0)
                .actualValue(250.0)
                .build();
    }
    
    @Test
    void createAnomaly_SavesAndReturns() {
        when(anomalyRepository.save(any(Anomaly.class)))
                .thenReturn(testAnomaly);

        Anomaly result = anomalyService.createAnomaly(
                "meter-1", "reading-1", 180.0, 250.0);

        assertNotNull(result);
        assertEquals(AnomalyStatus.OPEN, result.getStatus());
        assertEquals(250.0, result.getActualValue());
        verify(anomalyRepository, times(1)).save(any(Anomaly.class));
    }

    @Test
    void findById_AnomalyExists_ReturnsAnomaly() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));

        Anomaly result = anomalyService.findById("anomaly-1");

        assertNotNull(result);
        assertEquals("meter-1", result.getMeterId());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(anomalyRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                anomalyService.findById("bad-id")
        );
    }

    @Test
    void assign_UpdatesStatusToInProgress() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));
        when(anomalyRepository.save(any(Anomaly.class)))
                .thenReturn(testAnomaly);

        Anomaly result = anomalyService.assign("anomaly-1", "tech-1");

        assertEquals(AnomalyStatus.IN_PROGRESS, result.getStatus());
        assertEquals("tech-1", result.getAssignedTo());
    }

    @Test
    void resolve_WithNotes_SetsStatusToResolved() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));
        when(anomalyRepository.save(any(Anomaly.class)))
                .thenReturn(testAnomaly);

        Anomaly result = anomalyService.resolve(
                "anomaly-1", "Meter replaced on site.", "tech-1");

        assertEquals(AnomalyStatus.RESOLVED, result.getStatus());
        assertNotNull(result.getResolvedAt());
        assertEquals("Meter replaced on site.", result.getResolutionNotes());
    }

    @Test
    void resolve_EmptyNotes_ThrowsException() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));

        assertThrows(IllegalArgumentException.class, () ->
                anomalyService.resolve("anomaly-1", "", "tech-1")
        );
    }

    @Test
    void escalate_SetsStatusToEscalated() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));
        when(anomalyRepository.save(any(Anomaly.class)))
                .thenReturn(testAnomaly);

        Anomaly result = anomalyService.escalate("anomaly-1");

        assertEquals(AnomalyStatus.ESCALATED, result.getStatus());
    }

    @Test
    void autoResolve_SetsStatusToAutoResolved() {
        when(anomalyRepository.findById("anomaly-1"))
                .thenReturn(Optional.of(testAnomaly));
        when(anomalyRepository.save(any(Anomaly.class)))
                .thenReturn(testAnomaly);

        Anomaly result = anomalyService.autoResolve("anomaly-1");

        assertEquals(AnomalyStatus.AUTO_RESOLVED, result.getStatus());
        assertNotNull(result.getResolvedAt());
    }

    @Test
    void countOpen_ReturnsCorrectCount() {
        when(anomalyRepository.countByStatus(AnomalyStatus.OPEN))
                .thenReturn(3L);

        long count = anomalyService.countOpen();

        assertEquals(3L, count);
    }

    @Test
    void findByStatus_ReturnsMatchingAnomalies() {
        when(anomalyRepository.findByStatus(AnomalyStatus.OPEN))
                .thenReturn(List.of(testAnomaly));

        List<Anomaly> result = anomalyService.findByStatus(AnomalyStatus.OPEN);

        assertEquals(1, result.size());
        assertEquals(AnomalyStatus.OPEN, result.get(0).getStatus());
    }
}