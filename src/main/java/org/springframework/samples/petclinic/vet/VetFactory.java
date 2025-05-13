package org.springframework.samples.petclinic.vet;

import java.util.Set;

public class VetFactory {

	private VetFactory() {
		// Private constructor to prevent instantiation
	}

	public static Vet createVet(String firstName, String lastName, Set<Specialty> specialties) {
		Vet vet = new Vet();
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		if (specialties != null) {
			specialties.forEach(vet::addSpecialty);
		}
		return vet;
	}

	public static Specialty createSpecialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialty;
	}
}
