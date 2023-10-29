package ru.nsu.ccfit.networks.places.services;

import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;

public interface WeatherService {

    WeatherDTO realtimeWeather(Double lat, Double lng);
}
