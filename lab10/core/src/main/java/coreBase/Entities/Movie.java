package coreBase.Entities;

import lombok.*;

import javax.persistence.Entity;

/**
 * @author Rares.
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Movie extends BaseEntity<Integer> {

    private String title;
    private int price;
}
