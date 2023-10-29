package ru.nsu.ccfit.networks.places.models.converters;

import org.modelmapper.AbstractConverter;
import ru.nsu.ccfit.networks.places.models.api.responses.PlaceResponse;
import ru.nsu.ccfit.networks.places.models.dtos.PlaceDTO;

public class PlaceToPlaceDTOConverter
    extends AbstractConverter<PlaceResponse, PlaceDTO> {

    @Override
    protected PlaceDTO convert(PlaceResponse source) {
        PlaceDTO placeDTO = new PlaceDTO();

        if (source == null) {
            return placeDTO;
        }

        placeDTO.setName(source.getName());

        if (source.getPoint() != null) {
            placeDTO.setLat(source.getPoint().getLat());
            placeDTO.setLng(source.getPoint().getLon());
        }
        if (source.getInfo() != null) {
            placeDTO.setDescription(source.getInfo().getDescr());
            placeDTO.setImageUrl(source.getImage());
        }
        if (source.getAddress() != null) {
            StringBuilder addressBuilder = new StringBuilder();
            if (source.getAddress().getPostcode() != null) {
                addressBuilder.append(source.getAddress().getPostcode()).append(", ");
            }
            if (source.getAddress().getCountry() != null) {
                addressBuilder.append(source.getAddress().getCountry()).append(", ");
            }
            if (source.getAddress().getState() != null) {
                addressBuilder.append(source.getAddress().getState()).append(", ");
            }
            if (source.getAddress().getCity() != null) {
                addressBuilder.append(source.getAddress().getCity()).append(", ");
            }
            if (source.getAddress().getRoad() != null) {
                addressBuilder.append(source.getAddress().getRoad()).append(", ");
            }
            if (source.getAddress().getHouseNumber() != null) {
                addressBuilder.append(source.getAddress().getHouseNumber());
            }
            placeDTO.setAddress(addressBuilder.toString().replaceAll(", $", ""));
        }

        return placeDTO;
    }
}
