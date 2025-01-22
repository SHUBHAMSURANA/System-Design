class Singleton {

    // Private constructor to prevent instantiation
    private Singleton() {
        // Prevent instantiation via reflection
        if (InstanceHolder.INSTANCE != null) {
            throw new IllegalStateException("Instance already created!");
        }
    }

    // Static inner class - inner classes are not loaded until they are referenced
    private static class InstanceHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    // Public method to provide access to the singleton instance
    public static Singleton getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // Example method for testing the Singleton
    public void showMessage() {
        System.out.println("Hello from Singleton!");
    }
}

public class Main {
    public static void main(String[] args) {
        // Create four threads
        Runnable task = () -> {
            Singleton instance = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() + " obtained: " + instance);
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");
        Thread t4 = new Thread(task, "Thread-4");

        // Start all threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}


/*
Thread-1 obtained: Singleton@7a81197d
Thread-2 obtained: Singleton@7a81197d
Thread-3 obtained: Singleton@7a81197d
Thread-4 obtained: Singleton@7a81197d
Here, all threads print the same instance address, confirming the Singleton is thread-safe and only one instance is created.

The Singleton class is loaded into memory, but the static nested class InstanceHolder is not loaded yet. 
This delay is because the JVM loads inner classes lazily, only when they're accessed for the first time.
*/
