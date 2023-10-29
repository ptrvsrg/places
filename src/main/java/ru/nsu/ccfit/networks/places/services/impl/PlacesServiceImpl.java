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
import ru.nsu.ccfit.networks.places.models.api.responses.PlaceErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.PlaceResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.SimpleFeatureErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.SimpleFeatureResponse;
import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.PlacesNearbyException;
import ru.nsu.ccfit.networks.places.services.PlacesService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlacesServiceImpl
        implements PlacesService {

    private static final String API_URL = "https://api.opentripmap.com/0.1/ru/places";
    private static final Integer RADIUS = 1000;

    @Value("${onetripmap.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public List<PlaceDTO> listPlacesNearby(Double lat, Double lng) {
        String apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .path("/radius")
                .queryParam("apikey", "{apikey}")
                .queryParam("format", "json")
                .queryParam("radius", "{radius}")
                .queryParam("lat", "{lat}")
                .queryParam("lon", "{lon}")
                .encode()
                .toUriString();
        Map<String, Object> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("radius", RADIUS);
        params.put("lat", lat);
        params.put("lon", lng);

        ResponseEntity<String> response = restTemplate.getForEntity(apiUri, String.class, params);

        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            SimpleFeatureErrorResponse error = objectMapper.readValue(response.getBody(), SimpleFeatureErrorResponse.class);
            throw new PlacesNearbyException(error.getError());
        }

        SimpleFeatureResponse[] simpleFeatures = objectMapper.readValue(response.getBody(), SimpleFeatureResponse[].class);
        List<String> xids = Arrays.stream(simpleFeatures)
                .map(SimpleFeatureResponse::getXid)
                .toList();
        return searchPlacesByXid(xids);
    }

    @SneakyThrows
    private List<PlaceDTO> searchPlacesByXid(List<String> xids) {
        List<PlaceDTO> places = new ArrayList<>();

        UriComponentsBuilder apiUriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .path("/xid/{xid}")
                .queryParam("apikey", apiKey);

        for (String xid : xids) {
            String apiUri = apiUriBuilder.buildAndExpand(Map.of("xid", xid)).toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUri, String.class);

            if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                PlaceErrorResponse error = objectMapper.readValue(response.getBody(), PlaceErrorResponse.class);
                throw new PlacesNearbyException(error.getError());
            }

            PlaceResponse place = objectMapper.readValue(response.getBody(), PlaceResponse.class);
            places.add(modelMapper.map(place, PlaceDTO.class));
        }

        return places;
    }
}