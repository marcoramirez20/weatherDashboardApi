package com.WeatherDashboard.WeatherDashboard.service;

import com.WeatherDashboard.WeatherDashboard.domain.City;
import com.WeatherDashboard.WeatherDashboard.domain.Weather;
import com.WeatherDashboard.WeatherDashboard.domain.WeatherRequest;

public interface ForescastIO {
    Weather getWeather(WeatherRequest weatherRequest, City city);
}
