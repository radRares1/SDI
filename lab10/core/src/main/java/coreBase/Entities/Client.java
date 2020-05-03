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
public class Client extends BaseEntity<Integer> {

    private String name;
    private int age;

}
