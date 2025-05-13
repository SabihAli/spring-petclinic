package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

/**
 * Domain object representing a person with a first and last name.
 */
@MappedSuperclass
public abstract class Person extends BaseEntity {

	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	@Column(name = "last_name")
	@NotBlank
	private String lastName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName != null ? firstName.trim() : null;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName != null ? lastName.trim() : null;
	}

	/**
	 * Returns the full name of the person.
	 *
	 * @return firstName + " " + lastName
	 */
	public String getFullName() {
		return (firstName + " " + lastName).trim();
	}

	@Override
	public String toString() {
		return getFullName();
	}
}
