package ru.nsu.ccfit.networks.places.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.nsu.ccfit.networks.places.services.GeocodeService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final GeocodeService geocodeService;

    @GetMapping
    public Mono<Rendering> searchPage(@RequestParam(name = "place_name", required = false) String placeName) {
        if (placeName != null && !placeName.isEmpty()) {
            return geocodeService.forwardGeocode(placeName)
                    .flatMap(geocodes -> Mono.just(Rendering.view("search")
                            .modelAttribute("place_name", placeName)
                            .modelAttribute("places", geocodes)
                            .build()));
        }
        return Mono.just(Rendering.view("search")
                .build());
    }
}
