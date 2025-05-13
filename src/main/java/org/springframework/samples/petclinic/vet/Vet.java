
package org.springframework.samples.petclinic.vet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.model.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */

@Entity
@Table(name = "vets")
public class Vet extends Person {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "vet_specialties",
		joinColumns = @JoinColumn(name = "vet_id"),
		inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	private Set<Specialty> specialties = new HashSet<>();

	/**
	 * Returns the specialties sorted by name.
	 */
	@XmlElement
	public List<Specialty> getSpecialties() {
		return specialties.stream()
			.sorted(Comparator.comparing(NamedEntity::getName))
			.collect(Collectors.toList());
	}

	/**
	 * Returns the raw (unsorted) internal set of specialties.
	 */
	protected Set<Specialty> getSpecialtiesInternal() {
		return this.specialties;
	}

	public int getNrOfSpecialties() {
		return specialties.size();
	}

	public void addSpecialty(Specialty specialty) {
		this.specialties.add(specialty);
	}

	public boolean hasSpecialty(Specialty specialty) {
		return this.specialties.contains(specialty);
	}
}
