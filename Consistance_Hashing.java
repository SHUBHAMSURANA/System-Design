 // Key Takeaways
//  Your original code just distributed users, but now it stores and replicates data.
//  Data is stored on primary and backup servers to prevent data loss.
//  If a server is removed, data can still be retrieved from a replica. 

package implementations.java.consistent_hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHashing {
    private final int numReplicas;
    private final TreeMap<Long, String> ring;
    private final Set<String> servers;
    private final Map<String, Map<String, String>> serverData; // Stores data per server

    public ConsistentHashing(List<String> servers, int numReplicas) {
        this.numReplicas = numReplicas;
        this.ring = new TreeMap<>();
        this.servers = new HashSet<>();
        this.serverData = new HashMap<>();

        for (String server : servers) {
            addServer(server);
        }
    }

    private long hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            return ((long) (digest[0] & 0xFF) << 24) |
                   ((long) (digest[1] & 0xFF) << 16) |
                   ((long) (digest[2] & 0xFF) << 8) |
                   ((long) (digest[3] & 0xFF));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public void addServer(String server) {
        servers.add(server);
        serverData.put(server, new HashMap<>()); // Initialize storage for the server
        for (int i = 0; i < numReplicas; i++) {
            long hash = hash(server + "-" + i);
            ring.put(hash, server);
        }
    }

    public void removeServer(String server) {
        if (servers.remove(server)) {
            serverData.remove(server); // Remove stored data for this server
            for (int i = 0; i < numReplicas; i++) {
                long hash = hash(server + "-" + i);
                ring.remove(hash);
            }
        }
    }

    public List<String> getReplicatedServers(String key, int numReplicas) {
        if (ring.isEmpty()) return Collections.emptyList();

        long hash = hash(key);
        List<String> replicaServers = new ArrayList<>();
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);

        for (int i = 0; i < numReplicas; i++) {
            if (entry == null) entry = ring.firstEntry();
            replicaServers.add(entry.getValue());
            entry = ring.higherEntry(entry.getKey());
        }
        return replicaServers;
    }

    public void storeData(String key, String value) {
        List<String> replicatedServers = getReplicatedServers(key, 2); // Store on 2 servers
        for (String server : replicatedServers) {
            serverData.get(server).put(key, value);
            System.out.println("Stored data for '" + key + "' on " + server);
        }
    }

    public String getData(String key) {
        List<String> replicatedServers = getReplicatedServers(key, 2);
        for (String server : replicatedServers) {
            if (serverData.get(server).containsKey(key)) {
                System.out.println("Retrieving data for '" + key + "' from " + server);
                return serverData.get(server).get(key);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("S0", "S1", "S2", "S3", "S4", "S5");
        ConsistentHashing ch = new ConsistentHashing(servers, 3);

        // Store data
        ch.storeData("UserA", "Profile data for UserA");
        ch.storeData("UserB", "Profile data for UserB");

        // Retrieve data
        System.out.println("UserA's Data: " + ch.getData("UserA"));
        System.out.println("UserB's Data: " + ch.getData("UserB"));

        // Remove a server (simulate failure)
        ch.removeServer("S2");
        System.out.println("S2 is removed!");

        // Try to get data again
        System.out.println("UserA's Data after S2 removal: " + ch.getData("UserA"));
    }
}
