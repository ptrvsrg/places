package ru.nsu.ccfit.networks.places.services;

import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;

import java.util.List;

public interface GeocodeService {

    Mono<List<GeocodeDTO>> forwardGeocode(String name);
    Mono<List<GeocodeDTO>> reverseGeocode(Double lat, Double lng);
}
