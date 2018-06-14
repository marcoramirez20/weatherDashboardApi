package com.WeatherDashboard.WeatherDashboard.controller;

import com.WeatherDashboard.WeatherDashboard.domain.ForecastIOCity;
import com.WeatherDashboard.WeatherDashboard.domain.InputData;
import com.WeatherDashboard.WeatherDashboard.domain.Weather;
import com.WeatherDashboard.WeatherDashboard.service.ForescastIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/weather")
@RestController
public class WeatherController {

    @Autowired
    private ForescastIO forescastIO;

    @RequestMapping(method = RequestMethod.POST, value = "/getWeather")
    @ResponseBody
    public Weather getWeather(@RequestBody InputData inputData){
        if(inputData == null){
            throw new IllegalArgumentException("Request is null");
        }
        ForecastIOCity forecastIOCity = getForecastIOCity(inputData.getCity());

        Weather weather = forescastIO.getWeather(String.valueOf(forecastIOCity.getLatitude()),
                String.valueOf(forecastIOCity.getLongitude()), inputData.getUnitMeasure());
        weather.setCityName(forecastIOCity.getCityName());

        return weather;
    }

    private ForecastIOCity getForecastIOCity(String cityName){
        ForecastIOCity forecastIOCity = new ForecastIOCity();
        switch (cityName) {
            /*case "Santiago":
                forecastIOCity.setCityName("Santiago");
                forecastIOCity.setId(3871336);
                forecastIOCity.setLatitude(-33.4378d);
                forecastIOCity.setLongitude(-70.6504d);
                break;*/
            case "Caracas":
                forecastIOCity.setCityName("Caracas");
                forecastIOCity.setId(3646738);
                forecastIOCity.setLatitude(10.5061d);
                forecastIOCity.setLongitude(-66.9146d);
                break;
            case "Lima":
                forecastIOCity.setCityName("Lima");
                forecastIOCity.setId(3936456);
                forecastIOCity.setLatitude(-12.0621d);
                forecastIOCity.setLongitude(-77.0365d);
                break;
            case "Bogota":
                forecastIOCity.setCityName("Bogota");
                forecastIOCity.setId(3688689);
                forecastIOCity.setLatitude(4.5981d);
                forecastIOCity.setLongitude(-74.0761d);
                break;
            case "Buenos Aires":
                forecastIOCity.setId(3433955);
                forecastIOCity.setCityName("Buenos Aires");
                forecastIOCity.setLatitude(-34.6076d);
                forecastIOCity.setLongitude(-58.4371d);
                break;
            default:
                forecastIOCity.setCityName("Santiago");
                forecastIOCity.setId(3871336);
                forecastIOCity.setLatitude(-33.4378d);
                forecastIOCity.setLongitude(-70.6504d);
                break;
        }
        return forecastIOCity;
    }
}
