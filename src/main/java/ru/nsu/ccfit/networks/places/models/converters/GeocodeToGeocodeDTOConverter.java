package ru.nsu.ccfit.networks.places.models.converters;

import org.modelmapper.AbstractConverter;
import ru.nsu.ccfit.networks.places.models.api.responses.GeocodeResponse;
import ru.nsu.ccfit.networks.places.models.dtos.GeocodeDTO;

public class GeocodeToGeocodeDTOConverter
    extends AbstractConverter<GeocodeResponse.GeocodingLocation, GeocodeDTO> {

    @Override
    protected GeocodeDTO convert(GeocodeResponse.GeocodingLocation source) {
        GeocodeDTO geocodeDTO = new GeocodeDTO();

        if (source == null) {
            return geocodeDTO;
        }

        geocodeDTO.setName(source.getName());

        if (source.getPoint() != null) {
            geocodeDTO.setLat(source.getPoint().getLat());
            geocodeDTO.setLng(source.getPoint().getLng());
        }

        StringBuilder addressBuilder = new StringBuilder();
        if (source.getPostcode() != null) {
            addressBuilder.append(source.getPostcode()).append(", ");
        }
        if (source.getCountry() != null) {
            addressBuilder.append(source.getCountry()).append(", ");
        }
        if (source.getState() != null) {
            addressBuilder.append(source.getState()).append(", ");
        }
        if (source.getCity() != null) {
            addressBuilder.append(source.getCity()).append(", ");
        }
        if (source.getStreet() != null) {
            addressBuilder.append(source.getStreet()).append(", ");
        }
        if (source.getHousenumber() != null) {
            addressBuilder.append(source.getHousenumber());
        }
        geocodeDTO.setAddress(addressBuilder.toString().replaceAll(", $", ""));

        return geocodeDTO;
    }
}
