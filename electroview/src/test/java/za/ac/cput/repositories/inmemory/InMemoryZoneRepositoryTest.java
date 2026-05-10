package za.ac.cput.repositories.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.Enums.ZoneStatus;
import za.ac.cput.domain.Zone;
import za.ac.cput.repositories.IZoneRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryZoneRepositoryTest {

    private IZoneRepository repository;
    private Zone testZone;

    @BeforeEach
    void setUp() {
        repository = new InMemoryZoneRepository();

        testZone = Zone.builder()
                .name("Bellville")
                .description("Bellville distribution zone")
                .location("Western Cape")
                .capacityKwh(5000.0)
                .build();
    }

    @Test
    void save_NewZone_ReturnsSavedZone() {
        Zone saved = repository.save(testZone);

        assertNotNull(saved);
        assertEquals(testZone.getZoneId(), saved.getZoneId());
        assertEquals(1, repository.count());
    }

    @Test
    void findById_ExistingZone_ReturnsZone() {
        repository.save(testZone);

        Optional<Zone> found = repository.findById(testZone.getZoneId());

        assertTrue(found.isPresent());
        assertEquals("Bellville", found.get().getName());
    }

    @Test
    void findAll_MultipleZones_ReturnsAll() {
        Zone zone2 = Zone.builder()
                .name("Khayelitsha")
                .description("Khayelitsha zone")
                .location("Western Cape")
                .capacityKwh(8000.0)
                .build();

        repository.save(testZone);
        repository.save(zone2);

        List<Zone> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void deleteById_ExistingZone_RemovesZone() {
        repository.save(testZone);

        repository.delete(testZone.getZoneId());

        assertEquals(0, repository.count());
    }

    @Test
    void existsByName_AfterSave_ReturnsTrue() {
        repository.save(testZone);

        assertTrue(repository.existsByName("Bellville"));
    }

    @Test
    void existsByName_CaseInsensitive_ReturnsTrue() {
        repository.save(testZone);

        assertTrue(repository.existsByName("BELLVILLE"));
    }

    @Test
    void existsByName_NonExistent_ReturnsFalse() {
        assertFalse(repository.existsByName("Mitchells Plain"));
    }

    @Test
    void findByStatus_ReturnsMatchingZones() {
        testZone.activate();
        repository.save(testZone);

        List<Zone> normal = repository.findByStatus(ZoneStatus.NORMAL);

        assertEquals(1, normal.size());
    }
}