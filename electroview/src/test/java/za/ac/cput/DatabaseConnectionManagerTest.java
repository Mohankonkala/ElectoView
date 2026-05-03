package za.ac.cput;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.patterns.singleton.DatabaseConnectionManager;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionManagerTest {

    @AfterEach
    void resetSingleton() {
        DatabaseConnectionManager.resetInstance();
    }

    @Test
    void getInstance_ReturnsSameInstanceOnMultipleCalls() {
        DatabaseConnectionManager a = DatabaseConnectionManager
                .getInstance("jdbc:mysql://localhost/ev", "root", 10);
        DatabaseConnectionManager b = DatabaseConnectionManager
                .getInstance("jdbc:mysql://localhost/ev", "root", 10);

        assertSame(a, b);
    }

    @Test
    void getInstance_WithoutInit_ThrowsException() {
        assertThrows(IllegalStateException.class,
                DatabaseConnectionManager::getInstance);
    }

    @Test
    void acquireConnection_IncrementsActiveCount() {
        DatabaseConnectionManager mgr = DatabaseConnectionManager
                .getInstance("jdbc:test", "user", 5);

        assertEquals(0, mgr.getActiveConnections());
        mgr.acquireConnection();
        assertEquals(1, mgr.getActiveConnections());
    }

    @Test
    void releaseConnection_DecrementsActiveCount() {
        DatabaseConnectionManager mgr = DatabaseConnectionManager
                .getInstance("jdbc:test", "user", 5);

        String conn = mgr.acquireConnection();
        mgr.releaseConnection(conn);

        assertEquals(0, mgr.getActiveConnections());
    }

    @Test
    void acquireConnection_ExceedsPoolSize_ThrowsException() {
        DatabaseConnectionManager mgr = DatabaseConnectionManager
                .getInstance("jdbc:test", "user", 2);

        mgr.acquireConnection();
        mgr.acquireConnection();

        assertThrows(IllegalStateException.class,
                mgr::acquireConnection);
    }

    @Test
    void isConnected_AfterInit_ReturnsTrue() {
        DatabaseConnectionManager mgr = DatabaseConnectionManager
                .getInstance("jdbc:test", "user", 5);

        assertTrue(mgr.isConnected());
    }

    @Test
    void shutdown_SetsConnectedToFalse() {
        DatabaseConnectionManager mgr = DatabaseConnectionManager
                .getInstance("jdbc:test", "user", 5);

        mgr.shutdown();

        assertFalse(mgr.isConnected());
        assertEquals(0, mgr.getActiveConnections());
    }

    @Test
    void threadSafety_TwoThreads_GetSameInstance()
            throws InterruptedException {

        DatabaseConnectionManager[] results =
                new DatabaseConnectionManager[2];

        Thread t1 = new Thread(() -> results[0] =
                DatabaseConnectionManager.getInstance(
                        "jdbc:pg://localhost/ev", "admin", 10));
        Thread t2 = new Thread(() -> results[1] =
                DatabaseConnectionManager.getInstance(
                        "jdbc:pg://localhost/ev", "admin", 10));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertSame(results[0], results[1]);
    }
}