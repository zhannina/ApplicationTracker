package com.example.zsarsenbayev.applicationtracker;

import java.util.Date;

/**
 * Created by zsarsenbayev on 5/19/18.
 */

public class Emotions {

    private String objectId; // not needed to be created, only if you need access to them
    private Date created; // not needed to be created
    private Date updated; // not needed to be created

    private double anger;
    private double arousal;
    private double contempt;
    private double deviceID;
    private double disgust;
    private double fear;
    private double joy;
    private double sadness;
    private double surprise;
    private double timestamp;
    private double valence;

    public Emotions(){
        // set everything to null ?
    }

    public double getAnger() {
        return anger;
    }

    public void setAnger(double anger) {
        this.anger = anger;
    }

    public double getArousal() {
        return arousal;
    }

    public void setArousal(double arousal) {
        this.arousal = arousal;
    }

    public double getContempt() {
        return contempt;
    }

    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    public double getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(double deviceID) {
        this.deviceID = deviceID;
    }

    public double getDisgust() {
        return disgust;
    }

    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    public double getFear() {
        return fear;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public double getJoy() {
        return joy;
    }

    public void setJoy(double joy) {
        this.joy = joy;
    }

    public double getSadness() {
        return sadness;
    }

    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    public double getSurprise() {
        return surprise;
    }

    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double getValence() {
        return valence;
    }

    public void setValence(double valence) {
        this.valence = valence;
    }
}
