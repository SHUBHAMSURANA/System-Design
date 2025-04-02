package ProxyPattern;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// Key Takeaways
//  Your original code just distributed users, but now it stores and replicates data.
//  Data is stored on primary and backup servers to prevent data loss.
//  If a server is removed, data can still be retrieved from a replica.

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
        // we will get Key: 589295435 S0
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
        System.out.println("replicate sevelist" +replicatedServers.toString());
        String data = null;
        String g = null;
        String v =  null;
        for (String server : replicatedServers) {

            if (serverData.get(server).containsKey(key)) {
                System.out.println("Retrieving data'" + key + "' from " + server);
                g = key;
                v = serverData.get(server).get(key);
                data =  serverData.get(server).get(key);
            }
            else {
                System.out.println("other replica server did not has data , lets set it");
                if (g != null && v !=null) {
                    System.out.println("insisdee");
                    HashMap<String,String> t = new HashMap<>();
                    t.put(g,v);
                    serverData.put(server,t);
                }
            }
        }
        return data;
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("S0", "S1", "S2", "S3", "S4", "S5");
        ConsistentHashing ch = new ConsistentHashing(servers, 3);

        for (Map.Entry<Long, String> e : ch.ring.entrySet()) {
            System.out.println("Key: "+ e.getKey() + " "+  "VALUE " + e.getValue());
        }

        System.out.println("Key: "+ ch.hash("UserA"));

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
        System.out.println("UserA's Data after S2 removal: " + ch.getData("UserA"));
    }
}

//Output
//Key: 589295435 VALUE S0
//Key: 1044658961 VALUE S4
//Key: 1505973471 VALUE S2
//Key: 1862304784 VALUE S1
//Key: 2005620009 VALUE S4
//Key: 2180323102 VALUE S3
//Key: 2261640609 VALUE S4
//Key: 2494996994 VALUE S1
//Key: 2591652941 VALUE S1
//Key: 3004079326 VALUE S3
//Key: 3252108172 VALUE S5
//Key: 3256693828 VALUE S2
//Key: 3507599374 VALUE S3
//Key: 3600025915 VALUE S0
//Key: 4034829281 VALUE S0
//Key: 4107600006 VALUE S5
//Key: 4168880445 VALUE S2
//Key: 4194994702 VALUE S5
//Key: 1044696993
//Stored data for 'UserA' on S2
//Stored data for 'UserA' on S1
//Stored data for 'UserB' on S5
//Stored data for 'UserB' on S2
//replicate sevelist[S2, S1]
//Retrieving data'UserA' from S2
//Retrieving data'UserA' from S1
//UserA's Data: Profile data for UserA
//replicate sevelist[S5, S2]
//Retrieving data'UserB' from S5
//Retrieving data'UserB' from S2
//UserB's Data: Profile data for UserB
//S2 is removed!
//replicate sevelist[S1, S4]
//Retrieving data'UserA' from S1
//other replica server did not has data , lets set it
//        insisdee
//UserA's Data after S2 removal: Profile data for UserA
//replicate sevelist[S1, S4]
//Retrieving data'UserA' from S1
//Retrieving data'UserA' from S4
//UserA's Data after S2 removal: Profile data for UserA
