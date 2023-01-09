package io.weather.weather_by_ip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherAppController {

    private final WeatherAppService weatherAppService;

    public WeatherAppController(WeatherAppService weatherAppService) {
        this.weatherAppService = weatherAppService;
    }

    @GetMapping
    public String getWeather() {
        String ip = weatherAppService.getIpAddress();
        GeoLocation location = weatherAppService.getLocationFromIp(ip);
        return weatherAppService.getWeatherDataForIP(ip, location);
    }
}

