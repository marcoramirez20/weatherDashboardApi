package com.WeatherDashboard.WeatherDashboard.service;

import com.WeatherDashboard.WeatherDashboard.domain.DailyWeather;
import com.WeatherDashboard.WeatherDashboard.domain.Weather;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ForescastIOImpl implements ForescastIO {

    private static final String requestMethod = "GET";
    private static final String address = "https://api.darksky.net/forecast";
    private static final String key = "/5596433666541c0c3d7d72d7c364159a";
    private static final String exclude = "?exclude=minutely,hourly";
    private static final String unit = "&units=";
    private static final String unitsSI = "si";
    private static final String unitsUs = "us";
    private static final int codeHTTPOK = 200;
    private static final String applicationJson = "application/json";
    private static final String requestAccept = "Accept";
    private static final long timestampConstant = 1000l;

    @Override
    public Weather getWeather(String latitude, String longitude, String temperatureUnit) {
        Weather weather = new Weather();
        try {
            URL url = new URL(address+key+"/"+latitude+","+longitude+exclude+unit+getTemperatureUnits(temperatureUnit));
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
                weather = getWeatherFromJson(new JSONObject(output));
                break;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }

    private String getTemperatureUnits(String temperatureUnit){
        if ("F".equalsIgnoreCase(temperatureUnit)) {
            return unitsUs;
        } else {
            return unitsSI;
        }
    }

    private Weather getWeatherFromJson(JSONObject jsonObject) {
        Weather weather = new Weather();
        try {
            JSONObject aux = (JSONObject) jsonObject.get("currently");
            weather.setTemperature(Double.valueOf(String.valueOf(aux.get("temperature"))));
            weather.setDate((new Timestamp(Long.valueOf(String.valueOf(aux.get("time")))*timestampConstant)).toLocalDateTime()
                    .toLocalDate());
            weather.setDescription((String) aux.get("summary"));
            aux = (JSONObject) jsonObject.get("daily");
            JSONArray daily = (JSONArray) aux.get("data");
            List<DailyWeather> dailyWeathers = new ArrayList<>(20);
            DailyWeather dailyWeather;
            Iterator<Object> iterator = daily.iterator();
            while(iterator.hasNext()){
                dailyWeather = new DailyWeather();
                aux = (JSONObject) iterator.next();
                dailyWeather.setDescription((String) aux.get("summary"));
                dailyWeather.setMinTemperature(Double.valueOf(String.valueOf(aux.get("temperatureLow"))));
                dailyWeather.setMaxTemperature(Double.valueOf(String.valueOf(aux.get("temperatureHigh"))));
                dailyWeather.setDate((new Timestamp(Long.valueOf(String.valueOf(aux.get("time")))*timestampConstant))
                        .toLocalDateTime().toLocalDate());
                if(weather.getDate().compareTo(dailyWeather.getDate()) == 0){
                    weather.setMinTemperature(dailyWeather.getMinTemperature());
                    weather.setMaxTemperature(dailyWeather.getMaxTemperature());
                } else {
                    dailyWeathers.add(dailyWeather);
                }
            }
            weather.setDailyWeathers(dailyWeathers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }
}
