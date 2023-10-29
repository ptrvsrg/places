package ru.nsu.ccfit.networks.places.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.services.GeocodeService;
import ru.nsu.ccfit.networks.places.services.PlacesService;
import ru.nsu.ccfit.networks.places.services.WeatherService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final GeocodeService geocodeService;
    private final WeatherService weatherService;
    private final PlacesService placesService;

    @GetMapping
    public Mono<Rendering> placePage(@RequestParam(name = "lat") Double lat,
                                     @RequestParam(name = "lng") Double lng) {
        if (lat == null || lng == null) {
            return Mono.just(Rendering.view("place").build());
        }

        return geocodeService.reverseGeocode(lat, lng)
                .flatMap(geocodes -> {
                    if (geocodes == null || geocodes.isEmpty()) {
                        return Mono.just(Rendering.view("place").build());
                    }

                    return weatherService.realtimeWeather(lat, lng)
                            .flatMap(weather -> placesService.listPlacesNearby(lat, lng)
                                    .flatMap(placesNearby -> Mono.just(Rendering.view("place")
                                            .modelAttribute("place", geocodes.get(0))
                                            .modelAttribute("weather", weather)
                                            .modelAttribute("places_nearby", placesNearby)
                                            .build())));
                });
    }
}
