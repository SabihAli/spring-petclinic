package org.springframework.samples.petclinic.owner;

import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Utility class to handle pet-related logic for an owner.
 */
public class OwnerPetHelper {

	private final OwnerRepository ownerRepository;

	public OwnerPetHelper(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public static Pet getPetByName(Owner owner, String name, boolean ignoreNew) {
		Assert.notNull(owner, "Owner must not be null");
		for (Pet pet : owner.getPets()) {
			if (pet.getName() != null && pet.getName().equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	public static Pet getPetById(Owner owner, Integer id) {
		Assert.notNull(owner, "Owner must not be null");
		for (Pet pet : owner.getPets()) {
			if (!pet.isNew() && id.equals(pet.getId())) {
				return pet;
			}
		}
		return null;
	}

	/**
	 * Finds an owner by their ID.
	 * @param ownerId The ID of the owner.
	 * @return The owner.
	 */
	public Owner findOwnerById(int ownerId) {
		Optional<Owner> optionalOwner = this.ownerRepository.findById(ownerId);
		return optionalOwner.orElseThrow(() -> new IllegalArgumentException(
			"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));
	}

	public Pet findPetByOwnerId(int ownerId, Integer petId) {
		Owner owner = findOwnerById(ownerId);
		if (petId == null) {
			return new Pet();
		}
		return getPetById(owner, petId);
	}

	public static void addVisitToPet(Owner owner, Integer petId, Visit visit) {
		Assert.notNull(owner, "Owner must not be null!");
		Assert.notNull(petId, "Pet ID must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		Pet pet = getPetById(owner, petId);
		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);
	}

	/**
	 * Updates an existing pet or adds a new one to the owner.
	 * @param owner The owner of the pet.
	 * @param pet The pet with updated details.
	 */
	public void updateOrAddPet(Owner owner, Pet pet) {
		Pet existingPet = getPetById(owner, pet.getId());
		if (existingPet != null) {
			// Update existing pet's properties
			existingPet.setName(pet.getName());
			existingPet.setBirthDate(pet.getBirthDate());
			existingPet.setType(pet.getType());
		} else {
			// If pet doesn't exist, add it to the owner
			owner.addPet(pet);
		}
		this.ownerRepository.save(owner);  // Persist the changes to the repository
	}
}
