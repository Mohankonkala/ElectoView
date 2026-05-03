package za.ac.cput.patterns.singleton;

public class DatabaseConnectionManager {

    private static volatile DatabaseConnectionManager instance;

    private final String url;
    private final String username;
    private int activeConnections;
    private final int maxPoolSize;
    private boolean connected;

    private DatabaseConnectionManager(String url,
                                       String username,
                                       int maxPoolSize) {
        this.url             = url;
        this.username        = username;
        this.maxPoolSize     = maxPoolSize;
        this.activeConnections = 0;
        this.connected       = false;
        initialisePool();
    }

    // Thread-safe double-checked locking
    public static DatabaseConnectionManager getInstance(String url,
                                                        String username,
                                                        int maxPoolSize) {
        if (instance == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionManager(
                            url, username, maxPoolSize);
                }
            }
        }
        return instance;
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null)
            throw new IllegalStateException(
                    "Not initialised. Call getInstance(url, username, poolSize) first.");
        return instance;
    }

    // For testing only — never call in production
    public static void resetInstance() {
        instance = null;
    }

    private void initialisePool() {
        this.connected = true;
        System.out.println("[DB] Pool initialised: "
                + url + " (max=" + maxPoolSize + ")");
    }

    public String acquireConnection() {
        if (activeConnections >= maxPoolSize)
            throw new IllegalStateException(
                    "Connection pool exhausted (max=" + maxPoolSize + ").");
        activeConnections++;
        return "conn-" + activeConnections;
    }

    public void releaseConnection(String connectionId) {
        if (activeConnections > 0) activeConnections--;
    }

    public void shutdown() {
        this.connected = false;
        this.activeConnections = 0;
    }

    public String getUrl()            { return url; }
    public String getUsername()       { return username; }
    public int getActiveConnections() { return activeConnections; }
    public int getMaxPoolSize()       { return maxPoolSize; }
    public boolean isConnected()      { return connected; }

    @Override
    public String toString() {
        return "DatabaseConnectionManager{url='" + url
               + "', active=" + activeConnections
               + "/" + maxPoolSize + "}";
    }
}