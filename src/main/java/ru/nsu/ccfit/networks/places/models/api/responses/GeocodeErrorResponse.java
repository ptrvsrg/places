package ru.nsu.ccfit.networks.places.models.api.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeocodeErrorResponse {

    private String message;
    private List<String> hints;
}
