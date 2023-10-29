package ru.nsu.ccfit.networks.places.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.ccfit.networks.places.models.converters.CurrentWeatherToWeatherDTOConverter;
import ru.nsu.ccfit.networks.places.models.converters.GeocodeToGeocodeDTOConverter;
import ru.nsu.ccfit.networks.places.models.converters.PlaceToPlaceDTOConverter;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new GeocodeToGeocodeDTOConverter());
        modelMapper.addConverter(new CurrentWeatherToWeatherDTOConverter());
        modelMapper.addConverter(new PlaceToPlaceDTOConverter());
        return modelMapper;
    }
}
