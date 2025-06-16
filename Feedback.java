package com.smarthealthcare.model;

abstract class BaseEntity {
    protected int id;

    public BaseEntity() {
       
    }

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void displayInfo();
}


class InvalidRatingException extends Exception {
    public InvalidRatingException(String message) {
        super(message);
    }
}


public class Feedback extends BaseEntity {
    private int appointmentId;
    private int rating;          
    private String comments;


    public Feedback() {
        super();
    }


    public Feedback(int feedbackId, int appointmentId, int rating, String comments) throws InvalidRatingException {
        super(feedbackId);
        this.appointmentId = appointmentId;
        setRating(rating);
        this.comments = comments;
    }


    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) throws InvalidRatingException {
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public void displayInfo() {
        System.out.println("---------- Feedback Info ----------");
        System.out.println("Feedback ID     : " + id);
        System.out.println("Appointment ID  : " + appointmentId);
        System.out.println("Rating          : " + rating);
        System.out.println("Comments        : " + comments);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + id +
                ", appointmentId=" + appointmentId +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                '}';
    }
}
