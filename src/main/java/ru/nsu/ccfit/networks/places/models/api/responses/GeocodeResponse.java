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
public class GeocodeResponse {

    private List<GeocodingLocation> hits;
    private Long took;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GeocodingLocation {

        private GeocodingPoint point;
        private String osmId;
        private String osmType;
        private String osmKey;
        private String name;
        private String country;
        private String city;
        private String state;
        private String street;
        private String housenumber;
        private String postcode;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class GeocodingPoint {

            private Double lat;
            private Double lng;
        }
    }
}
