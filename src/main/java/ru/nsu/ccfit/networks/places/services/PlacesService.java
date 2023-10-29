package ru.nsu.ccfit.networks.places.services;

import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;

import java.util.List;

public interface PlacesService {

    Mono<List<PlaceDTO>> listPlacesNearby(Double lat, Double lng);
}
