package org.springframework.samples.petclinic.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VetService {

	private final VetRepository vetRepository;

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	public Collection<Vet> getAllVets() {
		return vetRepository.findAll();
	}

	public Page<Vet> getPaginatedVets(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return vetRepository.findAll(pageable);
	}

	public Vets getAllAsVetsObject() {
		Vets vets = new Vets();
		vets.getVetList().addAll(getAllVets());
		return vets;
	}
}
