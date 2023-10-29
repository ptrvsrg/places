package ru.nsu.ccfit.networks.places.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherDTO {

    private Long temperature;
    private Long feelsLikeTemperature;
    private Long windSpeed;
    private String windDirection;
    private Long pressure;
    private Long humidity;
    private String iconUrl;
    private String desc;
}
