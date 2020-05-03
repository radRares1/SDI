package coreBase.Entities;


/*
This class is needed as a many to many relation between clients and movies
to properly rent movies
 */

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class RentAction extends BaseEntity<Integer> {
    private int clientId;
    private int movieId;


}
