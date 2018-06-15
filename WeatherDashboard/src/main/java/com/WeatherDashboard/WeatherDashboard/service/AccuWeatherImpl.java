package com.WeatherDashboard.WeatherDashboard.service;

import com.WeatherDashboard.WeatherDashboard.domain.City;
import com.WeatherDashboard.WeatherDashboard.domain.DailyWeather;
import com.WeatherDashboard.WeatherDashboard.domain.Weather;
import com.WeatherDashboard.WeatherDashboard.domain.WeatherRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AccuWeatherImpl implements AccuWeather {

    private static final String requestMethod = "GET";
    private static final String address = "http://dataservice.accuweather.com/";
    private static final String currentContions = "currentconditions/v1/";
    private static final String fiveDaysOfDailyForecasts = "forecasts/v1/daily/5day/";
    private static final String keyParameter = "?apikey=";
    private static final String metricParameter = "&metric=";
    private static final String key = "EbK8f1YYdomGiDUoM2UIR6TXTOVjxzsT";
    private static final String applicationJson = "application/json";
    private static final String requestAccept = "Accept";
    private static final long timestampConstant = 1000l;
    private static final int codeHTTPOK = 200;

    @Override
    public Weather getWeather(WeatherRequest weatherRequest, City city) {
        Weather weather;

        weather = getCurrentConditions(weatherRequest, city);
        weather = get5DaysOfDailyForecasts(weather, weatherRequest, city);

        return weather;
    }

    private Weather get5DaysOfDailyForecasts(Weather weather, WeatherRequest weatherRequest, City city){
        try {
            URL url = new URL(address+fiveDaysOfDailyForecasts+city.getAccuWeatherId()+keyParameter+key
                    +metricParameter+ifTemperatureMetricUnits(weatherRequest.getUnitMeasure()));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty(requestAccept, applicationJson);

            if (codeHTTPOK != conn.getResponseCode()) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                weather = get5DaysOfDailyForecastsFromJson(weather, new JSONObject(output));
                break;
            }
            conn.disconnect();

        } catch (Exception e) {
            throw new RuntimeException("Exception : " + e.getMessage());
        }
        return weather;
    }

    private Weather get5DaysOfDailyForecastsFromJson(Weather weather, JSONObject jsonObject) {
        try {
            JSONArray daily = (JSONArray) jsonObject.get("DailyForecasts");
            JSONObject aux;
            List<DailyWeather> dailyWeathers = new ArrayList<>(20);
            DailyWeather dailyWeather;
            Iterator<Object> iterator = daily.iterator();
            while(iterator.hasNext()){
                dailyWeather = new DailyWeather();
                aux = (JSONObject) iterator.next();
                dailyWeather.setDate((new Timestamp(Long.valueOf(String.valueOf(aux.get("EpochDate")))
                        *timestampConstant)).toLocalDateTime().toLocalDate());
                JSONObject day = (JSONObject) aux.get("Day");
                JSONObject night = (JSONObject) aux.get("Night");
                dailyWeather.setDescription("Day: "+day.get("IconPhrase")+", Night: "+night.get("IconPhrase"));
                JSONObject min = (JSONObject)((JSONObject) aux.get("Temperature")).get("Minimum");
                dailyWeather.setMinTemperature(Double.valueOf(String.valueOf(min.get("Value"))));
                JSONObject max = (JSONObject)((JSONObject) aux.get("Temperature")).get("Maximum");
                dailyWeather.setMaxTemperature(Double.valueOf(String.valueOf(max.get("Value"))));
                if(weather.getDate().compareTo(dailyWeather.getDate()) == 0){
                    weather.setMinTemperature(dailyWeather.getMinTemperature());
                    weather.setMaxTemperature(dailyWeather.getMaxTemperature());
                } else {
                    dailyWeathers.add(dailyWeather);
                }
            }
            weather.setDailyWeathers(dailyWeathers);
        } catch (Exception e) {
            throw new RuntimeException("Exception : " + e.getMessage());
        }
        return weather;
    }

    private Weather getCurrentConditions(WeatherRequest weatherRequest, City city){
        Weather weather = new Weather();
        try {
            URL url = new URL(address+currentContions+city.getAccuWeatherId()+keyParameter+key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty(requestAccept, applicationJson);

            if (codeHTTPOK != conn.getResponseCode()) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                output = output.substring(1,output.length()-1);
                System.out.println(output);
                weather = getCurrentConditionsFromJson(new JSONObject(output), weatherRequest.getUnitMeasure());
                break;
            }
            conn.disconnect();

        } catch (Exception e) {
            throw new RuntimeException("Exception : " + e.getMessage());
        }
        return weather;
    }

    private boolean ifTemperatureMetricUnits(String temperatureUnit){
        if ("F".equalsIgnoreCase(temperatureUnit)) {
            return false;
        } else {
            return true;
        }
    }

    private Weather getCurrentConditionsFromJson(JSONObject jsonObject, String temperatureUnit) {
        Weather weather = new Weather();
        try {
            weather.setDescription((String) jsonObject.get("WeatherText"));
            weather.setDate((new Timestamp(Long.valueOf(String.valueOf(jsonObject.get("EpochTime")))*timestampConstant))
                    .toLocalDateTime().toLocalDate());
            JSONObject aux = (JSONObject)((JSONObject) jsonObject.get("Temperature"))
                    .get(ifTemperatureMetricUnits(temperatureUnit) ? "Metric" : "Imperial");
            weather.setTemperature(Double.valueOf(String.valueOf(aux.get("Value"))));
        } catch (Exception e) {
            throw new RuntimeException("Exception : " + e.getMessage());
        }
        return weather;
    }
}
