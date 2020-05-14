package ro.ubb.catalog.core.model;

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
