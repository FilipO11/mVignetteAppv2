package com.example.mvignetteappv2;

public class VehicleModel {

    private String vehicle_id;
    private String vehicle_name;
    private Object vignette;

    // Constructor
    public VehicleModel() {
        this.vehicle_id = "";
        this.vehicle_name = "";
        this.vignette = null;
    }
    public VehicleModel(String vehicle_id, String vehicle_name, Object vignette) {
        this.vehicle_id = vehicle_id;
        this.vehicle_name = vehicle_name;
        this.vignette = vignette;
    }

    // Getter and Setter
    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {

        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public Object getVignette() {
        return vignette;
    }

    public void setVignette(Object vignette) {
        this.vignette = vignette;
    }
}