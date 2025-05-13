package itmo.karenin.lab3.service;

import itmo.karenin.lab3.entity.Owner;
import itmo.karenin.lab3.exception.OwnerNotFoundException;
import itmo.karenin.lab3.mapping.OwnerMapper;
import itmo.karenin.lab3.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Transactional(readOnly = true)
    public Owner getOwnerById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found"));
    }

    @Transactional(readOnly = true)
    public Page<Owner> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }

    @Transactional
    public Owner createOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Transactional
    public Owner updateOwner(Long id, Owner owner) {
        Owner existingOwner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found"));
        ownerMapper.updateEntity(owner, existingOwner);
        return ownerRepository.save(existingOwner);
    }

    @Transactional
    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Owner getOwnerEntity(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found"));
    }

    @Transactional(readOnly = true)
    public Page<Owner> searchOwnersByName(String name, Pageable pageable) {
        return ownerRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}