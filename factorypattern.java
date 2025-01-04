public interface Vehicle {
    void drive();
}

public class Car implements Vehicle {
    public void drive() {
        System.out.println("Driving a car!");
    }
}
public class Truck implements Vehicle {
    public void drive() {
        System.out.println("Driving a truck!");
    }
}
public class Motorcycle implements Vehicle {
    public void drive() {
        System.out.println("Riding a motorcycle!");
    }
}

public class VehicleFactory {
    public static Vehicle getVehicle(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        switch (vehicleType) {
            case CAR:
                return new Car();
            case TRUCK:
                return new Truck();
            case MOTORCYCLE:
                return new Motorcycle();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}

public enum VehicleType {
    CAR, TRUCK, MOTORCYCLE
}

public class Main {
    public static void main(String[] args) {
        Vehicle car = VehicleFactory.getVehicle(VehicleType.CAR);
        car.drive();

        Vehicle truck = VehicleFactory.getVehicle(VehicleType.TRUCK);
        truck.drive();

        Vehicle motorcycle = VehicleFactory.getVehicle(VehicleType.MOTORCYCLE);
        motorcycle.drive();
    }
}
