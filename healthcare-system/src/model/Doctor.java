package model;

public class Doctor extends Person {
    private int doctorID;
    private String specialization;
    private String email;

    public Doctor() {}

    public Doctor(int doctorID, String name, String gender, String specialization, String email) throws InvalidEmailException {
        super(name, gender);
        this.doctorID = doctorID;
        this.specialization = specialization;
        setEmail(email); // validate using exception
    }

    public int getDoctorID() { return doctorID; }
    public void setDoctorID(int doctorID) { this.doctorID = doctorID; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getEmail() { return email; }
    public void setEmail(String email) throws InvalidEmailException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new InvalidEmailException("Invalid email format.");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorID=" + doctorID +
                ", name='" + getName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
