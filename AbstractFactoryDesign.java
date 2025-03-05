/*
Key Idea:
Instead of creating objects using a single factory, we create multiple factories, each responsible for creating a family of related objects.
*/

// Step 1: Create an interface for Products
interface Car {
    void assemble();
}

// Step 2: Implement Concrete Products
class LuxuryCar implements Car {
    public void assemble() {
        System.out.println("Assembling a Luxury Car");
    }
}

class EconomyCar implements Car {
    public void assemble() {
        System.out.println("Assembling an Economy Car");
    }
}

// Step 3: Create Factory Interface
interface CarFactory {
    Car createCar();
}

// Step 4: Implement Concrete Factories
class LuxuryCarFactory implements CarFactory {
    public Car createCar() {
        return new LuxuryCar();
    }
}

class EconomyCarFactory implements CarFactory {
    public Car createCar() {
        return new EconomyCar();
    }
}

// Step 5: Factory Producer
class FactoryProducer {
    public static CarFactory getFactory(String choice) {
        if (choice.equalsIgnoreCase("luxury")) {
            return new LuxuryCarFactory();
        } else if (choice.equalsIgnoreCase("economy")) {
            return new EconomyCarFactory();
        }
        return null;
    }
}

// Step 6: Usage
public class AbstractFactoryExample {
    public static void main(String[] args) {
        CarFactory luxuryFactory = FactoryProducer.getFactory("luxury");
        Car luxuryCar = luxuryFactory.createCar();
        luxuryCar.assemble();  // Output: Assembling a Luxury Car

        CarFactory economyFactory = FactoryProducer.getFactory("economy");
        Car economyCar = economyFactory.createCar();
        economyCar.assemble();  // Output: Assembling an Economy Car
    }
}
