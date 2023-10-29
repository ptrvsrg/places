package ru.nsu.ccfit.networks.places.models.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrentWeatherResponse {

    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Long visibility;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private Long dt;
    private Sys sys;
    private Long timezone;
    private Long id;
    private String name;
    private Long cod;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Coord {

        private Double lat;
        private Double lon;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Weather {

        private Long id;
        private String main;
        private String description;
        private String icon;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Main {

        private Double temp;
        private Double feelsLike;
        private Double tempMin;
        private Double tempMax;
        private Long pressure;
        private Long humidity;
        private Long seaLevel;
        private Long grndLevel;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Wind {

        private Double speed;
        private Long deg;
        private Double gust;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Clouds {

        private Long all;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Rain {

        @JsonProperty("1h")
        private Double h1;


        @JsonProperty("3h")
        private Double h3;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Snow {

        @JsonProperty("1h")
        private Double h1;


        @JsonProperty("3h")
        private Double h3;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Sys {

        private Long type;
        private Long id;
        private String country;
        private Long sunrise;
        private Long sunset;
    }
}
