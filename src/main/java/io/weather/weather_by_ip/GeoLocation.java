package io.weather.weather_by_ip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GeoLocation {
    private double latitude;
    private double longitude;
    private String city;
    private String country;


}

