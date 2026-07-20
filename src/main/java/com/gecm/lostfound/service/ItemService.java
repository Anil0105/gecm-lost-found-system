package com.gecm.lostfound.service;

import com.gecm.lostfound.dto.ItemView;
import com.gecm.lostfound.model.Item;
import com.gecm.lostfound.model.ItemStatus;
import com.gecm.lostfound.model.ItemType;
import com.gecm.lostfound.model.User;
import com.gecm.lostfound.repository.ItemRepository;
import com.gecm.lostfound.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ItemService(ItemRepository itemRepository,
                       UserRepository userRepository,
                       FileStorageService fileStorageService) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public String addItem(Integer userId, ItemType type, String title, String description,
                          String location, MultipartFile image) throws IOException {
        String imageName = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageName = fileStorageService.store(image);
            } catch (IllegalArgumentException ex) {
                return ex.getMessage();
            }
        }

        Item item = new Item();
        item.setType(type);
        item.setTitle(title.trim());
        item.setDescription(description.trim());
        item.setLocation(location.trim());
        item.setImage(imageName);
        item.setAddedBy(userId);
        item.setStatus(ItemStatus.pending);
        itemRepository.save(item);
        return null;
    }

    public List<ItemView> getApprovedRecent(int limit) {
        return enrich(itemRepository.findByStatusOrderByCreatedAtDesc(ItemStatus.approved))
                .stream()
                .limit(limit)
                .toList();
    }

    public List<ItemView> searchApproved(String typeFilter, String query) {
        ItemType type = null;
        if (typeFilter != null && !typeFilter.equals("all")) {
            type = ItemType.valueOf(typeFilter);
        }
        String q = query == null ? "" : query.trim();
        return enrich(itemRepository.searchApproved(type, q));
    }

    public List<ItemView> getUserItems(Integer userId) {
        return itemRepository.findByAddedByOrderByCreatedAtDesc(userId).stream()
                .map(this::toView)
                .toList();
    }

    public List<ItemView> getAllItemsWithUser() {
        return enrich(itemRepository.findAll().stream()
                .sorted((a, b) -> {
                    if (a.getCreatedAt() == null && b.getCreatedAt() == null) {
                        return 0;
                    }
                    if (a.getCreatedAt() == null) {
                        return 1;
                    }
                    if (b.getCreatedAt() == null) {
                        return -1;
                    }
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .toList());
    }

    public Optional<ItemView> getItemWithUser(Integer id) {
        return itemRepository.findById(id)
                .map(item -> {
                    ItemView view = toView(item);
                    userRepository.findById(item.getAddedBy()).ifPresent(user -> {
                        view.setAddedName(user.getName());
                        view.setAddedEmail(user.getEmail());
                    });
                    return view;
                });
    }

    @Transactional
    public void updateStatus(Integer id, ItemStatus status) {
        itemRepository.findById(id).ifPresent(item -> {
            item.setStatus(status);
            item.setUpdatedAt(LocalDateTime.now());
            itemRepository.save(item);
        });
    }

    public long countAll() {
        return itemRepository.count();
    }

    public long countByStatus(ItemStatus status) {
        return itemRepository.countByStatus(status);
    }

    private List<ItemView> enrich(List<Item> items) {
        Map<Integer, User> usersById = userRepository.findAll().stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return items.stream().map(item -> {
            ItemView view = toView(item);
            User user = usersById.get(item.getAddedBy());
            if (user != null) {
                view.setAddedName(user.getName());
                view.setAddedEmail(user.getEmail());
            }
            return view;
        }).toList();
    }

    private ItemView toView(Item item) {
        ItemView view = new ItemView();
        view.setId(item.getId());
        view.setType(item.getType());
        view.setTitle(item.getTitle());
        view.setDescription(item.getDescription());
        view.setLocation(item.getLocation());
        view.setImage(item.getImage());
        view.setAddedBy(item.getAddedBy());
        view.setStatus(item.getStatus());
        view.setCreatedAt(item.getCreatedAt());
        return view;
    }
}
