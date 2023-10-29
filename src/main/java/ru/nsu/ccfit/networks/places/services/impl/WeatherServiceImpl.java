package ru.nsu.ccfit.networks.places.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nsu.ccfit.networks.places.models.api.responses.CurrentWeatherErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.CurrentWeatherResponse;
import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.WeatherException;
import ru.nsu.ccfit.networks.places.services.WeatherService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl
    implements WeatherService {

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${openweather.api.key}")
    private String weatherApiKey;

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public WeatherDTO realtimeWeather(Double lat, Double lng) {
        String weatherApiUri = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("appid", "{appid}")
                .queryParam("lat", "{lat}")
                .queryParam("lon", "{lon}")
                .queryParam("lang", "ru")
                .queryParam("units", "metric")
                .encode()
                .toUriString();
        Map<String, Object> params = new HashMap<>();
        params.put("appid", weatherApiKey);
        params.put("lat", lat);
        params.put("lon", lng);

        ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUri, String.class, params);

        if (response.getStatusCode() != HttpStatus.OK) {
            CurrentWeatherErrorResponse error = objectMapper.readValue(response.getBody(),
                    CurrentWeatherErrorResponse.class);
            throw new WeatherException(error.getMessage());
        }

        CurrentWeatherResponse weather = objectMapper.readValue(response.getBody(), CurrentWeatherResponse.class);
        return modelMapper.map(weather, WeatherDTO.class);
    }
}
