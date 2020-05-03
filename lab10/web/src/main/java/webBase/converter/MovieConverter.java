package webBase.converter;

import coreBase.Entities.Movie;
import org.springframework.stereotype.Component;
import webBase.dto.MovieDto;

@Component
public class MovieConverter extends BaseConverter<Movie, MovieDto> {
    @Override
    public Movie convertDtoToModel(MovieDto dto) {
        Movie movie = Movie.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .build();
        movie.setId(dto.getId());
        return movie;
    }

    @Override
    public MovieDto convertModelToDto(Movie movie) {
        MovieDto dto = MovieDto.builder()
                .title(movie.getTitle())
                .price(movie.getPrice())
                .build();
        dto.setId(movie.getId());
        return dto;
    }
}
