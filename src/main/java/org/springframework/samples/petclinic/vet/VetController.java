
package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */

@Controller
class VetController {

	private final VetService vetService;

	public VetController(VetService vetService) {
		this.vetService = vetService;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		Page<Vet> paginated = vetService.getPaginatedVets(page, 5);
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", paginated.getContent());
		return "vets/vetList";
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		return vetService.getAllAsVetsObject();
	}
}
