package com.WeatherDashboard.WeatherDashboard.service;

import com.WeatherDashboard.WeatherDashboard.domain.Weather;

public interface ForescastIO {
    Weather getWeather(String latitude, String longitude, String temperatureUnit);
}
