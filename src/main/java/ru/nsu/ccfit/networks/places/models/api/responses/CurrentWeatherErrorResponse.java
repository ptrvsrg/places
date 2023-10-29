package ru.nsu.ccfit.networks.places.models.api.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrentWeatherErrorResponse {

    private Integer code;
    private String message;
}
