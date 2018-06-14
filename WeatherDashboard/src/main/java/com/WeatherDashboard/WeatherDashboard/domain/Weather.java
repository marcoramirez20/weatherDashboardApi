package com.WeatherDashboard.WeatherDashboard.domain;

import java.time.LocalDate;
import java.util.List;

public class Weather {

    public Weather() {
    }

    private String cityName;
    private Double temperature;
    private Double minTemperature;
    private Double maxTemperature;
    private LocalDate date;
    private String dayOfWeek;
    private String description;
    private List<DailyWeather> dailyWeathers;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public List<DailyWeather> getDailyWeathers() {
        return dailyWeathers;
    }

    public void setDailyWeathers(List<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
}
