package ru.nsu.ccfit.networks.places.services;

import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;

public interface WeatherService {

    Mono<WeatherDTO> realtimeWeather(Double lat, Double lng);
}
