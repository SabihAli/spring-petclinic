
package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 *
 * @author Juergen Hoeller
 */
@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {

}
