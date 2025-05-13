package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

// OwnerService.java
public interface OwnerService {
	Optional<Owner> findOwnerById(Integer id);

	void save(Owner owner);

	List<PetType> findPetTypes();

	Page<Owner> findPaginatedByLastName(String lastName, Pageable pageable);

	Page<Owner> findAll(Pageable pageable);

}
