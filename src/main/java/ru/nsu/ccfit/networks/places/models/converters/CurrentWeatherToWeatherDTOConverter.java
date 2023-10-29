package ru.nsu.ccfit.networks.places.models.converters;

import org.modelmapper.AbstractConverter;
import ru.nsu.ccfit.networks.places.models.api.responses.CurrentWeatherResponse;
import ru.nsu.ccfit.networks.places.models.dtos.WeatherDTO;

public class CurrentWeatherToWeatherDTOConverter
    extends AbstractConverter<CurrentWeatherResponse, WeatherDTO> {

    @Override
    protected WeatherDTO convert(CurrentWeatherResponse source) {
        WeatherDTO weatherDTO = new WeatherDTO();

        if (source == null) {
            return weatherDTO;
        }

        if (source.getMain() != null) {
            weatherDTO.setTemperature(Math.round(source.getMain().getTemp()));
            weatherDTO.setPressure(source.getMain().getPressure() * 3 / 4);
            weatherDTO.setHumidity(source.getMain().getHumidity());
            if (source.getMain().getFeelsLike() != null) {
                weatherDTO.setFeelsLikeTemperature(Math.round(source.getMain().getFeelsLike()));
            }
        }
        if (source.getWind() != null) {
            weatherDTO.setWindSpeed(Math.round(source.getWind().getSpeed()));
            weatherDTO.setWindDirection(getDirection(Double.valueOf(source.getWind().getDeg())));
        }
        if (source.getWeather() != null && !source.getWeather().isEmpty()) {
            weatherDTO.setIconUrl("https://openweathermap.org/img/wn/" + source.getWeather().get(0).getIcon() + "@2x.png");
            weatherDTO.setDesc(source.getWeather().get(0).getDescription());
        }

        return weatherDTO;
    }

    private String getDirection(Double degrees) {
        degrees = (degrees % 360 + 360) % 360;

        if ((degrees >= 0 && degrees < 22.5) || (degrees >= 337.5 && degrees <= 360)) {
            return "С";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "СВ";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "В";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "ЮВ";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "Ю";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "ЮЗ";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "З";
        } else {
            return "СЗ";
        }
    }
}
