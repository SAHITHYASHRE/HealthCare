import db.DBConnectivity;
// import model.Patient;
// import model.Doctor;
// import model.Appointment;
// import model.Feedback;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Connection conn = DBConnectivity.getConnection();
        if (conn == null) {
            System.out.println("Exiting: DB connection failed.");
            return;
        }

        while (true) {
            System.out.println("\n===== HEALTHCARE MENU =====");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Add Doctor");
            System.out.println("4. View Doctors");
            System.out.println("5. Add Appointment");
            System.out.println("6. View Appointments");
            System.out.println("7. Add Feedback");
            System.out.println("8. View Feedback");
            System.out.println("9. View Average Rating");
            System.out.println("10. Reschedule Appointment");
            System.out.println("11. Export Appointments");
            System.out.println("12. Export Feedback");
            System.out.println("13. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> addPatient(conn);
                case 2 -> viewPatients(conn);
                case 3 -> addDoctor(conn);
                case 4 -> viewDoctors(conn);
                case 5 -> addAppointment(conn);
                case 6 -> viewAppointments(conn);
                case 7 -> addFeedback(conn);
                case 8 -> viewFeedback(conn);
                case 9 -> viewDoctorAverageRating(conn);
                case 10 -> rescheduleAppointment(conn);
                case 11 -> exportDoctorAppointments(conn);
                case 12 -> exportDoctorFeedback(conn);
                case 13 -> {
                    System.out.println("Exiting system...");
                    try { conn.close(); } catch (SQLException ignored) {}
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addPatient(Connection conn) {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(sc.nextLine());

            System.out.print("Enter gender (Male/Female/Other): ");
            String gender = sc.nextLine();

            System.out.print("Enter contact number: ");
            String contact = sc.nextLine();

            System.out.print("Enter address: ");
            String address = sc.nextLine();

            String sql = "INSERT INTO Patient(name, age, gender, contact_number, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, contact);
            ps.setString(5, address);
            ps.executeUpdate();
            System.out.println("Patient added successfully.");
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewPatients(Connection conn) {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Patient")) {
            System.out.println("\n--- Patients ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Age: %d | Gender: %s | Contact: %s | Address: %s%n",
                        rs.getInt("patient_id"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("gender"), rs.getString("contact_number"), rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addDoctor(Connection conn) {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter specialization: ");
            String specialization = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO Doctor(name, specialization, email) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setString(3, email);
            ps.executeUpdate();
            System.out.println("Doctor added successfully.");
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewDoctors(Connection conn) {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Doctor")) {
            System.out.println("\n--- Doctors ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Specialization: %s | Email: %s%n",
                        rs.getInt("doctor_id"), rs.getString("name"),
                        rs.getString("specialization"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

private static void addAppointment(Connection conn) {
    try {
        conn.setAutoCommit(false);

        System.out.print("Enter patient ID: ");
        int patientId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter doctor ID: ");
        int doctorId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(sc.nextLine());

        System.out.print("Enter time slot: ");
        String timeSlot = sc.nextLine();

        System.out.print("Enter status: ");
        String status = sc.nextLine();

        String checkSql = "SELECT COUNT(*) FROM Appointment WHERE doctor_id = ? AND appointment_date = ? AND time_slot = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, doctorId);
        checkStmt.setDate(2, Date.valueOf(date));
        checkStmt.setString(3, timeSlot);
        ResultSet rs = checkStmt.executeQuery();

        rs.next();
        if (rs.getInt(1) > 0) {
            System.out.println("Doctor is already booked at that time slot. Please choose a different time.");
            conn.rollback();
            return;
        }

        String insertSql = "INSERT INTO Appointment(patient_id, doctor_id, appointment_date, time_slot, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setInt(1, patientId);
        insertStmt.setInt(2, doctorId);
        insertStmt.setDate(3, Date.valueOf(date));
        insertStmt.setString(4, timeSlot);
        insertStmt.setString(5, status);

        insertStmt.executeUpdate();
        conn.commit();

        System.out.println("Appointment added successfully.");

        insertStmt.close();
        checkStmt.close();
    } catch (Exception e) {
        try {
            conn.rollback();
            System.out.println("Error occurred. Transaction rolled back.");
        } catch (SQLException ex) {
            System.out.println("Rollback failed: " + ex.getMessage());
        }
        System.out.println("Error: " + e.getMessage());
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Failed to reset auto-commit: " + e.getMessage());
        }
    }
}


    private static void viewAppointments(Connection conn) {
        String sql = "SELECT a.appointment_id, p.name AS patient, d.name AS doctor, a.appointment_date, a.time_slot, a.status FROM Appointment a JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Appointments ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Patient: %s | Doctor: %s | Date: %s | Time: %s | Status: %s%n",
                        rs.getInt("appointment_id"), rs.getString("patient"), rs.getString("doctor"),
                        rs.getDate("appointment_date"), rs.getString("time_slot"), rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addFeedback(Connection conn) {
        try {
            System.out.print("Enter appointment ID: ");
            int appointmentId = Integer.parseInt(sc.nextLine());

            System.out.print("Enter rating (1-5): ");
            int rating = Integer.parseInt(sc.nextLine());

            System.out.print("Enter comments: ");
            String comments = sc.nextLine();

            String sql = "INSERT INTO Feedback(appointment_id, rating, comments) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ps.setInt(2, rating);
            ps.setString(3, comments);
            ps.executeUpdate();
            System.out.println("Feedback submitted.");
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewFeedback(Connection conn) {
        String sql = "SELECT f.feedback_id, p.name AS patient, d.name AS doctor, f.rating, f.comments FROM Feedback f JOIN Appointment a ON f.appointment_id = a.appointment_id JOIN Patient p ON a.patient_id = p.patient_id JOIN Doctor d ON a.doctor_id = d.doctor_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Feedback ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Patient: %s | Doctor: %s | Rating: %d | Comments: %s%n",
                        rs.getInt("feedback_id"), rs.getString("patient"), rs.getString("doctor"),
                        rs.getInt("rating"), rs.getString("comments"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewDoctorAverageRating(Connection conn) {
        System.out.println("Here is the list of doctors");
        viewDoctors(conn);
        try {
            System.out.print("Enter doctor ID: ");
            int doctorId = Integer.parseInt(sc.nextLine());

            String sql = """
                SELECT d.name AS doctor_name, AVG(f.rating) AS avg_rating
                FROM Feedback f
                JOIN Appointment a ON f.appointment_id = a.appointment_id
                JOIN Doctor d ON a.doctor_id = d.doctor_id
                WHERE d.doctor_id = ?
                GROUP BY d.name
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.printf("Doctor: %s | Average Rating: %.2f%n", rs.getString("doctor_name"), rs.getDouble("avg_rating"));
            } else {
                System.out.println("No feedback found for the given doctor ID.");
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void rescheduleAppointment(Connection conn) {
        System.out.println("Here are all the appointments with their ids");
        viewAppointments(conn);
        try {
            System.out.print("Enter Appointment ID to reschedule: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter new appointment date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine());
    
            System.out.print("Enter new time slot: ");
            String timeSlot = sc.nextLine();

            System.out.print("Enter new status (Pending/Completed/Cancelled): ");
            String status = sc.nextLine();

            String sql = "UPDATE Appointment SET appointment_date = ?, time_slot = ?, status = ? WHERE appointment_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, timeSlot);
            ps.setString(3, status);
            ps.setInt(4, id);
            int updated = ps.executeUpdate();

            if (updated > 0) System.out.println("Appointment rescheduled successfully.");
            else System.out.println("Appointment ID not found.");
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void exportDoctorAppointments(Connection conn) {
        try {
            System.out.print("Enter Doctor ID: ");
            int doctorId = Integer.parseInt(sc.nextLine());

            String sql = """
                SELECT a.appointment_id, a.appointment_date, a.time_slot, a.status,
                       p.name AS patient_name, p.age, p.gender, p.contact_number, p.address
                FROM Appointment a
                JOIN Patient p ON a.patient_id = p.patient_id
                WHERE a.doctor_id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            String fileName = "Doctor_" + doctorId + "_Appointments.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                while (rs.next()) {
                    String line = String.format("""
                            Appointment ID: %d
                            Date: %s | Time: %s | Status: %s
                            Patient Name: %s | Age: %d | Gender: %s | Contact: %s | Address: %s
                            --------------------------------------
                            """,
                            rs.getInt("appointment_id"), rs.getDate("appointment_date"), rs.getString("time_slot"),
                            rs.getString("status"), rs.getString("patient_name"), rs.getInt("age"),
                            rs.getString("gender"), rs.getString("contact_number"), rs.getString("address"));
                    writer.write(line);
                }
            }

            System.out.println("✅ Appointment details exported to " + fileName);
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("❌ Error exporting doctor appointments: " + e.getMessage());
        }
    }
    private static void exportDoctorFeedback(Connection conn) {
        try {
            System.out.print("Enter Doctor ID: ");
            int doctorId = Integer.parseInt(sc.nextLine());

            String sql = """
                SELECT f.feedback_id, f.rating, f.comments,
                       p.name AS patient_name, a.appointment_date
                FROM Feedback f
                JOIN Appointment a ON f.appointment_id = a.appointment_id
                JOIN Patient p ON a.patient_id = p.patient_id
                WHERE a.doctor_id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            String fileName = "Doctor_" + doctorId + "_Feedback.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                while (rs.next()) {
                    String line = String.format("""
                            Feedback ID: %d | Rating: %d
                            Comments: %s
                            Given By: %s | Appointment Date: %s
                            --------------------------------------
                            """,
                            rs.getInt("feedback_id"), rs.getInt("rating"),
                            rs.getString("comments"), rs.getString("patient_name"),
                            rs.getDate("appointment_date"));
                    writer.write(line);
                }
            }

            System.out.println("✅ Feedback exported to " + fileName);
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("❌ Error exporting feedback: " + e.getMessage());
        }
    }


}
