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
public class PlaceResponse {

    private String xid;
    private String name;
    private Address address;
    private String kinds;
    private String osm;
    private String wikidata;
    private String rate;
    private String image;
    private Preview preview;
    private String wikipedia;
    private WikipediaExtracts wikipediaExtracts;
    private String voyage;
    private String url;
    private String otm;
    private Sources sources;
    private Info info;
    private Bbox bbox;
    private Point point;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Address {

        private String city;
        private String road;
        private String house;
        private String state;
        private String county;
        private String suburb;
        private String country;
        private String postcode;
        private String countryCode;
        private String houseNumber;
        private String cityDistrict;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Preview {

        private String source;
        private Integer width;
        private Integer height;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class WikipediaExtracts {

        private String title;
        private String text;
        private String html;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Sources {

        private String geometry;
        private List<String> attributes;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Info {

        private String src;
        private String srcId;
        private String descr;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Bbox {

        private Double lonMin;
        private Double lonMax;
        private Double latMin;
        private Double latMax;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Point {

        private Double lon;
        private Double lat;
    }
}
