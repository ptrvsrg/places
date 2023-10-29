package ru.nsu.ccfit.networks.places.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.networks.places.services.GeocodeService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final GeocodeService geocodeService;

    @GetMapping
    public String searchPage(@RequestParam(name = "place_name", required = false) String placeName, Model model) {
        if (placeName != null && !placeName.isEmpty()) {
            model.addAttribute("place_name", placeName);
            model.addAttribute("places", geocodeService.forwardGeocode(placeName));
        }
        return "search";
    }
}
