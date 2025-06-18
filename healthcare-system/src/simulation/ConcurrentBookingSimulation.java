package simulation;

import model.Appointment;
import model.DuplicateBookingException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConcurrentBookingSimulation {

    public static void main(String[] args) {
        Appointment appointment = new Appointment(101, 1, 10,
                LocalDate.of(2025, 6, 20),
                LocalTime.of(10, 0),
                "Pending");

        Runnable bookingTask = () -> {
            synchronized (appointment) {
                System.out.println(Thread.currentThread().getName() + " is trying to book...");

                try {
                    Thread.sleep(1000);

                    // Check if appointment is already booked
                    if ("Confirmed".equalsIgnoreCase(appointment.getStatus())) {
                        throw new DuplicateBookingException("Appointment already booked.");
                    }

                    appointment.setStatus("Confirmed");
                    appointment.book();

                    System.out.println(Thread.currentThread().getName() + " booked the appointment.");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (DuplicateBookingException e) {
                    System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
                }
            }
        };

        Thread user1 = new Thread(bookingTask, "User-1");
        Thread user2 = new Thread(bookingTask, "User-2");

        user1.start();
        user2.start();
    }
}
