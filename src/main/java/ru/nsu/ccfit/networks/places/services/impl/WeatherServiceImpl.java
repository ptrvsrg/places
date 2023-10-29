package ru.nsu.ccfit.networks.places.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.api.responses.CurrentWeatherErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.CurrentWeatherResponse;
import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.WeatherException;
import ru.nsu.ccfit.networks.places.services.WeatherService;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${openweather.api.key}")
    private String weatherApiKey;

    private final ModelMapper modelMapper;


    @Cacheable("weather")
    @Override
    public Mono<WeatherDTO> realtimeWeather(Double lat, Double lng) {
        String weatherApiUri = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("appid", weatherApiKey)
                .queryParam("lat", lat)
                .queryParam("lon", lng)
                .queryParam("lang", "ru")
                .queryParam("units", "metric")
                .encode()
                .toUriString();
        return WebClient.create()
                .get()
                .uri(weatherApiUri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(CurrentWeatherErrorResponse.class)
                        .flatMap(error -> Mono.error(new WeatherException(error.getMessage()))))
                .bodyToMono(CurrentWeatherResponse.class)
                .map(weather -> modelMapper.map(weather, WeatherDTO.class));
    }
}