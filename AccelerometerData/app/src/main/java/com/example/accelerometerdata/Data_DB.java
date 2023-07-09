package com.example.accelerometerdata;

public class Data_DB {
    String time;
    float x,y,z;

    public Data_DB(float x, float y, float z, String t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = t;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public String getCurrentPeriod() {
        return time;
    }
}