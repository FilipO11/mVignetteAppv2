package com.example.mvignetteappv2;

public class FleetModel {

    private String fleet_id;
    private String fleet_name;

    // Constructor
    public FleetModel(String fleet_id, String fleet_name) {
        this.fleet_id = fleet_id;
        this.fleet_name = fleet_name;
    }

    // Getter and Setter
    public String getFleet_name() {
        return fleet_name;
    }

    public void setFleet_name(String fleet_name) {
        this.fleet_name = fleet_name;
    }

    public String getFleet_id() {
        return fleet_id;
    }

    public void setFleet_id(String fleet_id) {
        this.fleet_id = fleet_id;
    }
}
