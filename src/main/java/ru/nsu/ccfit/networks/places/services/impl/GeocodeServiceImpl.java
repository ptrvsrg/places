package ru.nsu.ccfit.networks.places.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nsu.ccfit.networks.places.models.api.responses.GeocodeErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.GeocodeResponse;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.GeocodeException;
import ru.nsu.ccfit.networks.places.services.GeocodeService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeocodeServiceImpl
        implements GeocodeService {

    private static final int PLACES_LIMIT = 100;
    private static final String API_URL = "https://graphhopper.com/api/1/geocode";

    @Value("${graphhopper.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public List<GeocodeDTO> forwardGeocode(String name) {
        String apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("key", "{key}")
                .queryParam("q", "{q}")
                .queryParam("limit", "{limit}")
                .encode()
                .toUriString();
        Map<String, Object> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("q", name);
        params.put("limit", PLACES_LIMIT);

        ResponseEntity<String> response = restTemplate.getForEntity(apiUri, String.class, params);

        if (response.getStatusCode() != HttpStatus.OK) {
            GeocodeErrorResponse error = objectMapper.readValue(response.getBody(), GeocodeErrorResponse.class);
            throw new GeocodeException(error.getHints());
        }

        GeocodeResponse locations = objectMapper.readValue(response.getBody(), GeocodeResponse.class);
        return locations
                .getHits()
                .stream()
                .map(geocodingLocation -> modelMapper.map(geocodingLocation, GeocodeDTO.class))
                .toList();
    }

    @SneakyThrows
    @Override
    public List<GeocodeDTO> reverseGeocode(Double lat, Double lng) {
        String apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("key", "{key}")
                .queryParam("point", "{point}")
                .queryParam("reverse", "{reverse}")
                .queryParam("limit", "{limit}")
                .encode()
                .toUriString();
        Map<String, Object> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("point", String.format("%f,%f", lat, lng));
        params.put("reverse", true);
        params.put("limit", PLACES_LIMIT);

        ResponseEntity<String> response = restTemplate.getForEntity(apiUri, String.class, params);

        if (response.getStatusCode() != HttpStatus.OK) {
            GeocodeErrorResponse error = objectMapper.readValue(response.getBody(), GeocodeErrorResponse.class);
            throw new GeocodeException(error.getHints());
        }

        GeocodeResponse locations = objectMapper.readValue(response.getBody(), GeocodeResponse.class);

        return locations
                .getHits()
                .stream()
                .map(geocodingLocation -> modelMapper.map(geocodingLocation, GeocodeDTO.class))
                .toList();
    }
}
