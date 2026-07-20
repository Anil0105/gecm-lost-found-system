package com.gecm.lostfound.service;

import com.gecm.lostfound.dto.ClaimView;
import com.gecm.lostfound.model.Claim;
import com.gecm.lostfound.model.ClaimStatus;
import com.gecm.lostfound.model.Item;
import com.gecm.lostfound.repository.ClaimRepository;
import com.gecm.lostfound.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;

    public ClaimService(ClaimRepository claimRepository, ItemRepository itemRepository) {
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void submitClaim(Integer itemId, String claimerName, String claimerEmail, String message) {
        Claim claim = new Claim();
        claim.setItemId(itemId);
        claim.setClaimerName(claimerName.trim());
        claim.setClaimerEmail(claimerEmail.trim());
        claim.setMessage(message == null ? "" : message.trim());
        claim.setStatus(ClaimStatus.requested);
        claimRepository.save(claim);
    }

    public List<ClaimView> getAllClaimsWithItemTitle() {
        Map<Integer, Item> itemsById = itemRepository.findAll().stream()
                .collect(Collectors.toMap(Item::getId, item -> item));

        return claimRepository.findAll().stream()
                .sorted(Comparator.comparing(Claim::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(claim -> {
                    ClaimView view = new ClaimView();
                    view.setId(claim.getId());
                    view.setItemId(claim.getItemId());
                    view.setClaimerName(claim.getClaimerName());
                    view.setClaimerEmail(claim.getClaimerEmail());
                    view.setMessage(claim.getMessage());
                    view.setStatus(claim.getStatus());
                    view.setCreatedAt(claim.getCreatedAt());
                    Item item = itemsById.get(claim.getItemId());
                    if (item != null) {
                        view.setItemTitle(item.getTitle());
                    }
                    return view;
                })
                .toList();
    }

    @Transactional
    public void updateStatus(Integer claimId, ClaimStatus status) {
        claimRepository.findById(claimId).ifPresent(claim -> {
            claim.setStatus(status);
            claimRepository.save(claim);
        });
    }

    public long countRequested() {
        return claimRepository.countByStatus(ClaimStatus.requested);
    }
}
