package com.WeatherDashboard.WeatherDashboard.controller;

import com.WeatherDashboard.WeatherDashboard.domain.City;
import com.WeatherDashboard.WeatherDashboard.domain.WeatherRequest;
import com.WeatherDashboard.WeatherDashboard.domain.Weather;
import com.WeatherDashboard.WeatherDashboard.service.AccuWeather;
import com.WeatherDashboard.WeatherDashboard.service.ForescastIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/weather")
@RestController
public class WeatherController {

    @Autowired
    private ForescastIO forescastIO;

    @Autowired
    private AccuWeather accuWeather;

    @RequestMapping(method = RequestMethod.POST, value = "/getWeather")
    @ResponseBody
    public Weather getWeather(@RequestBody WeatherRequest weatherRequest){
        Weather weather = new Weather();
        if(weatherRequest == null){
            throw new IllegalArgumentException("Request is null");
        }

        weather = chooseWeatherApiService(weatherRequest);

        return weather;
    }

    private Weather chooseWeatherApiService(WeatherRequest weatherRequest) {
        Weather weather = new Weather();

        City city = getCity(weatherRequest.getCity());
        switch (weatherRequest.getService()) {
            case "AccuWeather":
                weather = accuWeather.getWeather(weatherRequest, city);
                break;
            default:
                weather = forescastIO.getWeather(String.valueOf(city.getLatitude()),
                        String.valueOf(city.getLongitude()), weatherRequest.getUnitMeasure());
                break;
        }
        weather.setCityName(city.getCityName());
        return weather;
    }

    private City getCity(String cityName){
        City city = new City();
        switch (cityName) {
            case "Caracas":
                city.setCityName("Caracas");
                city.setOpenWeatherMapId(3646738);
                city.setAccuWeatherId(353020);
                city.setLatitude(10.5061d);
                city.setLongitude(-66.9146d);
                break;
            case "Lima":
                city.setCityName("Lima");
                city.setOpenWeatherMapId(3936456);
                city.setAccuWeatherId(264120);
                city.setLatitude(-12.0621d);
                city.setLongitude(-77.0365d);
                break;
            case "Bogota":
                city.setCityName("Bogotá");
                city.setOpenWeatherMapId(3688689);
                city.setAccuWeatherId(107487);
                city.setLatitude(4.5981d);
                city.setLongitude(-74.0761d);
                break;
            case "Buenos Aires":
                city.setCityName("Ciudad Autonoma de Buenos Aires");
                city.setOpenWeatherMapId(3433955);
                city.setAccuWeatherId(7894);
                city.setLatitude(-34.6076d);
                city.setLongitude(-58.4371d);
                break;
            default:
                city.setCityName("Santiago");
                city.setOpenWeatherMapId(3871336);
                city.setAccuWeatherId(60449);
                city.setLatitude(-33.4378d);
                city.setLongitude(-70.6504d);
                break;
        }
        return city;
    }
}
