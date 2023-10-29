package ru.nsu.ccfit.networks.places.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.api.responses.GeocodeErrorResponse;
import ru.nsu.ccfit.networks.places.models.api.responses.GeocodeResponse;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.GeocodeException;
import ru.nsu.ccfit.networks.places.services.GeocodeService;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeocodeServiceImpl implements GeocodeService {

    private static final int PLACES_LIMIT = 100;
    private static final String API_URL = "https://graphhopper.com/api/1/geocode";

    @Value("${graphhopper.api.key}")
    private String apiKey;

    private final ModelMapper modelMapper;

    @SneakyThrows
    @Override
    public Mono<List<GeocodeDTO>> forwardGeocode(String name) {
        URI apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("key", apiKey)
                .queryParam("q", name)
                .queryParam("limit", PLACES_LIMIT)
                .build()
                .toUri();
        return WebClient.create()
                .get()
                .uri(apiUri)
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.isSameCodeAs(HttpStatus.OK),
                        response -> response.bodyToMono(GeocodeErrorResponse.class)
                                .flatMap(error -> Mono.error(new GeocodeException(error.getHints()))))
                .bodyToMono(GeocodeResponse.class)
                .flatMap(locations -> Mono.just(locations.getHits())
                        .flatMapMany(Flux::fromIterable)
                        .map(geocodingLocation -> modelMapper.map(geocodingLocation, GeocodeDTO.class))
                        .collectList()
                );
    }

    @SneakyThrows
    @Override
    public Mono<List<GeocodeDTO>> reverseGeocode(Double lat, Double lng) {
        URI apiUri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("key", apiKey)
                .queryParam("point", String.format("%f,%f", lat, lng))
                .queryParam("reverse", true)
                .queryParam("limit", PLACES_LIMIT)
                .build()
                .toUri();
        return WebClient.create()
                .get()
                .uri(apiUri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(GeocodeErrorResponse.class)
                        .flatMap(error -> Mono.error(new GeocodeException(error.getHints()))))
                .bodyToMono(GeocodeResponse.class)
                .flatMap(locations -> Mono.just(locations.getHits())
                        .flatMapMany(Flux::fromIterable)
                        .map(geocodingLocation -> modelMapper.map(geocodingLocation, GeocodeDTO.class))
                        .collectList());
    }
}
