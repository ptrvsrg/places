package ru.nsu.ccfit.networks.places.services;

import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;

import java.util.List;

public interface GeocodeService {

    List<GeocodeDTO> forwardGeocode(String name);
    List<GeocodeDTO> reverseGeocode(Double lat, Double lng);
}
