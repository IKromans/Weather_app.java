package io.weather.weather_by_ip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherData {
    private double temperature;
    private int humidity;
    private String description;

    @Override
    public String toString() {
        return "There is " + description.toLowerCase() + " outside, temperature is " +
                 temperature +
                " degrees, and humidity is " + humidity +
                "%. "  + "Have a nice day!";
    }
}
