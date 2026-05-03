package za.ac.cput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.ZoneRepository;
import za.ac.cput.service.ZoneService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    private Zone testZone;

    @BeforeEach
    void setUp() {
        testZone = Zone.builder()
                .name("Bellville")
                .description("Bellville distribution zone")
                .location("Western Cape")
                .capacityKwh(5000.0)
                .build();
    }

    @Test
    void createZone_Success() {
        when(zoneRepository.existsByName("Bellville"))
                .thenReturn(false);
        when(zoneRepository.save(any(Zone.class)))
                .thenReturn(testZone);

        Zone result = zoneService.createZone(
                "Bellville", "Bellville distribution zone",
                "Western Cape", 5000.0);

        assertNotNull(result);
        assertEquals("Bellville", result.getName());
        verify(zoneRepository, times(1)).save(any(Zone.class));
    }

    @Test
    void createZone_DuplicateName_ThrowsException() {
        when(zoneRepository.existsByName("Bellville"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                zoneService.createZone(
                        "Bellville", "desc",
                        "Western Cape", 5000.0)
        );

        verify(zoneRepository, never()).save(any(Zone.class));
    }

    @Test
    void findById_ZoneExists_ReturnsZone() {
        when(zoneRepository.findById("zone-1"))
                .thenReturn(Optional.of(testZone));

        Zone result = zoneService.findById("zone-1");

        assertNotNull(result);
        assertEquals("Bellville", result.getName());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(zoneRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                zoneService.findById("bad-id")
        );
    }

    @Test
    void activate_SetsStatusToNormal() {
        when(zoneRepository.findById("zone-1"))
                .thenReturn(Optional.of(testZone));
        when(zoneRepository.save(any(Zone.class)))
                .thenReturn(testZone);

        Zone result = zoneService.activate("zone-1");

        assertEquals(ZoneStatus.NORMAL, result.getStatus());
    }

    @Test
    void updateThreshold_ValidValue_UpdatesThreshold() {
        when(zoneRepository.findById("zone-1"))
                .thenReturn(Optional.of(testZone));
        when(zoneRepository.save(any(Zone.class)))
                .thenReturn(testZone);

        Zone result = zoneService.updateThreshold(
                "zone-1", ThresholdType.ABSOLUTE, 4500.0);

        assertEquals(ThresholdType.ABSOLUTE, result.getThresholdType());
        assertEquals(4500.0, result.getThresholdValue());
    }

    @Test
    void updateThreshold_NegativeValue_ThrowsException() {
        when(zoneRepository.findById("zone-1"))
                .thenReturn(Optional.of(testZone));

        assertThrows(IllegalArgumentException.class, () ->
                zoneService.updateThreshold(
                        "zone-1", ThresholdType.ABSOLUTE, -100.0)
        );
    }

    @Test
    void findAll_ReturnsAllZones() {
        when(zoneRepository.findAll())
                .thenReturn(List.of(testZone));

        List<Zone> result = zoneService.findAll();

        assertEquals(1, result.size());
    }
}