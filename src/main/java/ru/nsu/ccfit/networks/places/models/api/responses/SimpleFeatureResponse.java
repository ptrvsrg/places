package ru.nsu.ccfit.networks.places.models.api.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SimpleFeatureResponse {

    private String xid;
    private String name;
    private String kinds;
    private String osm;
    private String wikidata;
    private Double dist;
    private Point point;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Point {

        private Double lon;
        private Double lat;
    }
}
