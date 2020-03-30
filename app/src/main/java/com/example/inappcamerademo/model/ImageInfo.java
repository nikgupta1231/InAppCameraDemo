package com.example.inappcamerademo.model;

public class ImageInfo {
    private String name;
    private long timestamp;

    private String path;

    private double lat;
    private double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
