public class Doctor {
    int doctorID;
    String name;
    String specialization;
    String email;
    Doctor(int doctorID, String name, String specialization, String email){
        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
    }
    public int getDoctorID(){
        return doctorID;
    }
    public String getName(){
        return name;
    }
    public String getSpecialization(){
        return specialization;
    }
    public String getEmail(){
        return email;
    }
    @Override
    public String toString() {
        return "Doctor [ID=" + doctorID + ", Name=" + name + ", Specialization=" + specialization + ", Email=" + email + "]";
    }

}
