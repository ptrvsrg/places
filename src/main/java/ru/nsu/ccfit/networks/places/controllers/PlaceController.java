package ru.nsu.ccfit.networks.places.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;
import ru.nsu.ccfit.networks.places.services.GeocodeService;
import ru.nsu.ccfit.networks.places.services.PlacesService;
import ru.nsu.ccfit.networks.places.services.WeatherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final GeocodeService geocodeService;
    private final WeatherService weatherService;
    private final PlacesService placesService;

    @GetMapping
    public String placePage(@RequestParam(name = "lat", defaultValue = "55.76") Double lat,
                            @RequestParam(name = "lng", defaultValue = "37.64") Double lng,
                            Model model) {
        List<GeocodeDTO> places = geocodeService.reverseGeocode(lat, lng);
        if (places == null || places.isEmpty()) {
            model.addAttribute("place", null);
            return "place";
        }

        model.addAttribute("place", places.get(0));
        model.addAttribute("weather", weatherService.realtimeWeather(lat, lng));
        model.addAttribute("places_nearby", placesService.listPlacesNearby(lat, lng));

        return "place";
    }
}
