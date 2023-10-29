package ru.nsu.ccfit.networks.places.models.exceptions;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GeocodeException
    extends Exception {

    private List<String> errors;
}
