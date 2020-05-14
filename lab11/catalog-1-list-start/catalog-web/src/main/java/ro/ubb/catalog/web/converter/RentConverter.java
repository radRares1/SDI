package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.RentAction;
import ro.ubb.catalog.web.dto.RentDto;

@Component
public class RentConverter extends BaseConverter<RentAction, RentDto> {
    @Override
    public RentAction convertDtoToModel(RentDto dto) {
        RentAction rent = RentAction.builder()
                .clientId(dto.getClientId())
                .movieId(dto.getMovieId())
                .build();
        rent.setId(dto.getId());
        return rent;
    }

    @Override
    public RentDto convertModelToDto(RentAction rentAction) {
        RentDto dto = RentDto.builder()
                .clientId(rentAction.getClientId())
                .movieId(rentAction.getMovieId())
                .build();
        dto.setId(rentAction.getId());
        return dto;
    }
}
