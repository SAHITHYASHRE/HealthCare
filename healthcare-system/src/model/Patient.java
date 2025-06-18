package model;

public class Patient extends Person {
    private int patientId;
    private int age;
    private String contactNumber;
    private String address;

    public Patient() {}

    public Patient(int patientId, String name, int age, String gender, String contactNumber, String address) throws InvalidAgeException {
        super(name, gender);
        this.patientId = patientId;
        setAge(age); // validate using exception
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getAge() { return age; }
    public void setAge(int age) throws InvalidAgeException {
        if (age <= 0) {
            throw new InvalidAgeException("Age must be greater than 0.");
        }
        this.age = age;
    }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + getName() + '\'' +
                ", age=" + age +
                ", gender='" + getGender() + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
