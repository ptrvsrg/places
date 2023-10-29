package ru.nsu.ccfit.networks.places.services;

import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;

import java.util.List;

public interface PlacesService {

    List<PlaceDTO> listPlacesNearby(Double lat, Double lng);
}
