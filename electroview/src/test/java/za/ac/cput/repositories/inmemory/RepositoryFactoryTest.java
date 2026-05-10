package za.ac.cput.factories;

import org.junit.jupiter.api.Test;
import za.ac.cput.repositories.IUserRepository;
import za.ac.cput.repositories.IZoneRepository;
import za.ac.cput.repositories.inmemory.InMemoryUserRepository;
import za.ac.cput.repositories.inmemory.InMemoryZoneRepository;
import za.ac.cput.repositories.filesystem.FileSystemUserRepository;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryFactoryTest {

    @Test
    void getUserRepository_InMemory_ReturnsInMemoryImpl() {
        IUserRepository repo = RepositoryFactory.getUserRepository(StorageType.IN_MEMORY);

        assertNotNull(repo);
        assertTrue(repo instanceof InMemoryUserRepository);
    }

    @Test
    void getUserRepository_FileSystem_ReturnsFileSystemStub() {
        IUserRepository repo = RepositoryFactory.getUserRepository(StorageType.FILE_SYSTEM);

        assertNotNull(repo);
        assertTrue(repo instanceof FileSystemUserRepository);
    }

    @Test
    void getUserRepository_Database_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                RepositoryFactory.getUserRepository(StorageType.DATABASE));
    }

    @Test
    void getZoneRepository_InMemory_ReturnsInMemoryImpl() {
        IZoneRepository repo = RepositoryFactory.getZoneRepository(StorageType.IN_MEMORY);

        assertNotNull(repo);
        assertTrue(repo instanceof InMemoryZoneRepository);
    }

    @Test
    void getMeterRepository_InMemory_NotNull() {
        assertNotNull(RepositoryFactory.getMeterRepository(StorageType.IN_MEMORY));
    }

    @Test
    void getReadingRepository_InMemory_NotNull() {
        assertNotNull(RepositoryFactory.getReadingRepository(StorageType.IN_MEMORY));
    }

    @Test
    void getAnomalyRepository_InMemory_NotNull() {
        assertNotNull(RepositoryFactory.getAnomalyRepository(StorageType.IN_MEMORY));
    }

    @Test
    void getReportRepository_InMemory_NotNull() {
        assertNotNull(RepositoryFactory.getReportRepository(StorageType.IN_MEMORY));
    }

    @Test
    void getDailySummaryRepository_InMemory_NotNull() {
        assertNotNull(RepositoryFactory.getDailySummaryRepository(StorageType.IN_MEMORY));
    }

    @Test
    void factoryInstances_AreIndependent() {
        IUserRepository repo1 = RepositoryFactory.getUserRepository(StorageType.IN_MEMORY);
        IUserRepository repo2 = RepositoryFactory.getUserRepository(StorageType.IN_MEMORY);

        // Each call returns a fresh instance — not a singleton
        assertNotSame(repo1, repo2);
    }
}