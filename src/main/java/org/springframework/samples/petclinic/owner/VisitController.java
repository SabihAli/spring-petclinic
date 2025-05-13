package org.springframework.samples.petclinic.owner;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
class VisitController {

	private final OwnerRepository ownerRepository;
	private final OwnerPetHelper ownerPetHelper;

	public VisitController(OwnerRepository ownerRepository, OwnerPetHelper ownerPetHelper) {
		this.ownerRepository = ownerRepository;
		this.ownerPetHelper = ownerPetHelper;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping method that needs a Visit.
	 */
	@ModelAttribute("visit")
	public Visit loadPetWithVisit(@PathVariable("ownerId") int ownerId,
								  @PathVariable("petId") int petId,
								  Map<String, Object> model) {

		Owner owner = ownerPetHelper.findOwnerById(ownerId);
		Pet pet = ownerPetHelper.findPetByOwnerId(ownerId, petId);

		model.put("owner", owner);
		model.put("pet", pet);

		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String initNewVisitForm() {
		return "pets/createOrUpdateVisitForm";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@ModelAttribute("owner") Owner owner,
									  @PathVariable("petId") int petId,
									  @Valid @ModelAttribute("visit") Visit visit,
									  BindingResult result,
									  RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}

		ownerPetHelper.addVisitToPet(owner, petId, visit);
		ownerRepository.save(owner);
		redirectAttributes.addFlashAttribute("message", "Your visit has been booked");
		return "redirect:/owners/{ownerId}";
	}
}
