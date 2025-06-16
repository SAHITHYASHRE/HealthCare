package com.cognizant.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
	
	private int appointment_id;
	private int doctor_id;
	private int patient_id;
	private LocalDate appointment_date;
	private LocalTime time_slot;
	private String status;
	
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Appointment(int appointment_id, int doctor_id, int patient_id, LocalDate appointment_date,
			LocalTime time_slot, String status) {
		super();
		this.appointment_id = appointment_id;
		this.doctor_id = doctor_id;
		this.patient_id = patient_id;
		this.appointment_date = appointment_date;
		this.time_slot = time_slot;
		this.status = status;
	}

	public int getAppointment_id() {
		return appointment_id;
	}

	public void setAppointment_id(int appointment_id) {
		this.appointment_id = appointment_id;
	}

	public int getDoctor_id() {
		return doctor_id;
	}

	public void setDoctor_id(int doctor_id) {
		this.doctor_id = doctor_id;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public LocalDate getAppointment_date() {
		return appointment_date;
	}

	public void setAppointment_date(LocalDate appointment_date) {
		this.appointment_date = appointment_date;
	}

	public LocalTime getTime_slot() {
		return time_slot;
	}

	public void setTime_slot(LocalTime time_slot) {
		this.time_slot = time_slot;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [appointment_id=" + appointment_id + ", doctor_id=" + doctor_id + ", patient_id="
				+ patient_id + ", appointment_date=" + appointment_date + ", time_slot=" + time_slot + ", status="
				+ status + "]";
	}
	
	

}
