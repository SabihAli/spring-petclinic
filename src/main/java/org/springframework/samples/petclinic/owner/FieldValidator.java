package org.springframework.samples.petclinic.owner;

import org.springframework.validation.Errors;

public interface FieldValidator {
	void validate(Pet pet, Errors errors);
}
