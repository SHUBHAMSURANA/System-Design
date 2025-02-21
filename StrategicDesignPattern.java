// Main Class
package com.example.designpattern;

import com.example.designpattern.Strategicdeignpattern.RiderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
package com.example.designpattern.Strategicdeignpattern;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class DesignpatternApplication implements CommandLineRunner{

	private final RiderService riderService;

	public DesignpatternApplication(RiderService riderService) {
		this.riderService = riderService;
	}


	public static void main(String[] args) {
		SpringApplication.run(DesignpatternApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Simulate booking a ride
		riderService.bookRide(1L, 101L, 25.0);


	}
}

//RiderService , The event is triggered here

@Service
public class RiderService {
    private final ApplicationEventPublisher eventPublisher;

    public RiderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        System.out.println("Injected ApplicationEventPublisher implementation: " + eventPublisher.getClass().getName());
    }

    public void bookRide(Long riderId, Long bookingId, Double fare) {
        System.out.println("Ride booked for Rider ID: " + riderId + ", Booking ID: " + bookingId);

        // Publish the RideBookedEvent
        RideBookedEvent event = new RideBookedEvent(riderId, bookingId, fare);
        eventPublisher.publishEvent(event);
    }
}

// PaymentService is called 
@Service
public class PaymentService {

    @EventListener
    public void handleRideBookedEvent(RideBookedEvent event) {
        System.out.println("Processing payment for Rider ID: " + event.getRiderId() +
                ", Booking ID: " + event.getBookingId() + ", Fare: $" + event.getFare());

        // Simulate payment processing
        System.out.println("Payment processed successfully for Booking ID: " + event.getBookingId());
    }
}


// RiderDetailClass
public class RideBookedEvent {
    private Long riderId;
    private Long bookingId;
    private Double fare;

    public RideBookedEvent(Long riderId, Long bookingId, Double fare) {
        this.riderId = riderId;
        this.bookingId = bookingId;
        this.fare = fare;
    }

    // Getters
    public Long getRiderId() {
        return riderId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Double getFare() {
        return fare;
    }
}

// to have async listner
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.context.event.EventListener;

@Service
public class NotificationService {

    @Async  // Runs in a separate thread
    @EventListener
    public void sendNotification(RideBookedEvent event) {
        System.out.println("Sending notification asynchronously for Booking ID: " + event.getBookingId());

        // Simulate delay
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("Notification sent for Booking ID: " + event.getBookingId());
    }
}
