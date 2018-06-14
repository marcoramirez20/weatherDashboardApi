package com.WeatherDashboard.WeatherDashboard.domain;

import java.time.LocalDate;

public class DailyWeather {

    public DailyWeather() {
    }

    private Double minTemperature;
    private Double maxTemperature;
    private LocalDate date;
    private String dayOfWeek;
    private String description;

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        dayOfWeek = this.date.getDayOfWeek().name();
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
