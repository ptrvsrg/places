package ru.nsu.ccfit.networks.places.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaceDTO {

    private Double lat;
    private Double lng;
    private String name;
    private String address;
    private String description;
    private String imageUrl;
}
