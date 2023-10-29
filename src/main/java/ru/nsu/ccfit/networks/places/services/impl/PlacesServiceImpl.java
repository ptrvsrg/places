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
import ru.nsu.ccfit.networks.places.models.api.responses.PlaceErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.PlaceResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.SimpleFeatureErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.SimpleFeatureResponse;
import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.PlacesNearbyException;
import ru.nsu.ccfit.networks.places.services.PlacesService;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacesServiceImpl
        implements PlacesService {

    private static final String API_URL = "https://api.opentripmap.com/0.1/ru/places";
    private static final Integer RADIUS = 500;

    @Value("${opentripmap.api.key}")
    private String apiKey;

    private final ModelMapper modelMapper;

    @Cacheable("places_nearby")
    @Override
    public Mono<List<PlaceDTO>> listPlacesNearby(Double lat, Double lng) {
        URI apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .path("/radius")
                .queryParam("apikey", apiKey)
                .queryParam("format", "json")
                .queryParam("radius", RADIUS)
                .queryParam("lat", lat)
                .queryParam("lon", lng)
                .build()
                .toUri();
        return WebClient.create()
                .get()
                .uri(apiUri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(SimpleFeatureErrorResponse.class)
                        .flatMap(error -> Mono.just(new PlacesNearbyException(error.getError()))))
                .bodyToFlux(SimpleFeatureResponse.class)
                .map(SimpleFeatureResponse::getXid)
                .flatMap(this::searchPlaceByXid)
                .map(placeResponse -> modelMapper.map(placeResponse, PlaceDTO.class))
                .collectList();
    }


    @Cacheable("xid_place")
    public Mono<PlaceResponse> searchPlaceByXid(String xid) {
        URI apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .path("/xid/" + xid)
                .queryParam("apikey", apiKey)
                .build()
                .toUri();
        return WebClient.create()
                .get()
                .uri(apiUri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(PlaceErrorResponse.class)
                        .flatMap(error -> Mono.error(new PlacesNearbyException(error.getError()))))
                .bodyToMono(PlaceResponse.class);
    }
}
