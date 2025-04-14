package com.example.weather.controller;

import com.example.weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String home() {
        return "weather";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            model.addAttribute("city", response.getName());
            model.addAttribute("temperature", response.getMain().getTemp());
            model.addAttribute("description", response.getWeather()[0].getDescription());
        } catch (Exception e) {
            model.addAttribute("error", "City not found. Please try again.");
        }
        return "weather";
    }
}
