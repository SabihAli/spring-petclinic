package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerRepository ownerRepository;
	private final OwnerPetHelper ownerPetHelper;
	private final PetValidator petValidator;

	public PetController(OwnerRepository ownerRepository, OwnerPetHelper ownerPetHelper, PetValidator petValidator) {
		this.ownerRepository = ownerRepository;
		this.ownerPetHelper = ownerPetHelper;
		this.petValidator = petValidator;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.ownerRepository.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable("ownerId") int ownerId,
					   @PathVariable(name = "petId", required = false) Integer petId) {
		if (petId == null) {
			return new Pet();
		}
		return ownerPetHelper.findPetByOwnerId(ownerId, petId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(petValidator);
	}

	@GetMapping("/pets/new")
	public String initCreationForm(Owner owner) {
		owner.addPet(new Pet());
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result,
									  RedirectAttributes redirectAttributes) {
		if (StringUtils.hasText(pet.getName()) && pet.isNew() && ownerPetHelper.getPetByName(owner, pet.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(LocalDate.now())) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		owner.addPet(pet);
		ownerRepository.save(owner);
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result,
									RedirectAttributes redirectAttributes) {
		if (StringUtils.hasText(pet.getName())) {
			Pet existingPet = ownerPetHelper.getPetByName(owner, pet.getName(), false);
			if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(LocalDate.now())) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		ownerPetHelper.updateOrAddPet(owner, pet);
		ownerRepository.save(owner);
		redirectAttributes.addFlashAttribute("message", "Pet details have been edited");
		return "redirect:/owners/{ownerId}";
	}
}
