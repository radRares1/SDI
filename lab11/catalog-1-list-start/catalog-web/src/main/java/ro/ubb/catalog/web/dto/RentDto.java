package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class RentDto extends BaseDto {

    private int clientId;
    private int movieId;
}
