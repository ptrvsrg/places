package ru.nsu.ccfit.networks.places.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;
import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;
import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;
import ru.nsu.ccfit.networks.places.models.exceptions.GeocodeException;
import ru.nsu.ccfit.networks.places.models.exceptions.PlacesNearbyException;
import ru.nsu.ccfit.networks.places.models.exceptions.WeatherException;
import ru.nsu.ccfit.networks.places.services.GeocodeService;
import ru.nsu.ccfit.networks.places.services.PlacesService;
import ru.nsu.ccfit.networks.places.services.WeatherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@Slf4j
public class PlaceController {

    private final GeocodeService geocodeService;
    private final WeatherService weatherService;
    private final PlacesService placesService;

    @GetMapping
    public Mono<Rendering> placePage(@RequestParam(name = "lat") Double lat,
                                     @RequestParam(name = "lng") Double lng) {
        if (lat == null || lng == null) {
            return Mono.just(Rendering.view("place")
                    .build());
        }

        Mono<List<GeocodeDTO>> geocodesMono = geocodeService.reverseGeocode(lat, lng)
                .doOnError(GeocodeException.class, e -> log.error(e.getErrors()
                        .stream()
                        .reduce("", (err1, err2) -> err1 + "; " + err2), e))
                .onErrorComplete();
        Mono<WeatherDTO> weatherMono = weatherService.realtimeWeather(lat, lng)
                .doOnError(WeatherException.class, e -> log.error(e.getLocalizedMessage(), e))
                .onErrorComplete();
        Mono<List<PlaceDTO>> placesMono = placesService.listPlacesNearby(lat, lng)
                .doOnError(PlacesNearbyException.class, e -> log.error(e.getLocalizedMessage(), e))
                .onErrorComplete();

        return Mono.zip(geocodesMono, weatherMono, placesMono)
                .flatMap(tuple -> {
                    List<GeocodeDTO> geocodes = tuple.getT1();
                    WeatherDTO weather = tuple.getT2();
                    List<PlaceDTO> placesNearby = tuple.getT3();

                    if (geocodes.isEmpty()) {
                        return Mono.just(Rendering.view("place")
                                .build());
                    }

                    return Mono.just(Rendering.view("place")
                            .modelAttribute("place", geocodes.get(0))
                            .modelAttribute("weather", weather)
                            .modelAttribute("places_nearby", placesNearby)
                            .build());
                });
    }
}
