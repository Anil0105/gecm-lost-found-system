package com.gecm.lostfound.repository;

import com.gecm.lostfound.model.Item;
import com.gecm.lostfound.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByAddedByOrderByCreatedAtDesc(Integer addedBy);

    long countByStatus(ItemStatus status);

    @Query("""
            SELECT i FROM Item i
            JOIN User u ON i.addedBy = u.id
            WHERE i.status = :status
            ORDER BY i.createdAt DESC
            """)
    List<Item> findByStatusOrderByCreatedAtDesc(@Param("status") ItemStatus status);

    @Query("""
            SELECT i FROM Item i
            JOIN User u ON i.addedBy = u.id
            WHERE i.status = com.gecm.lostfound.model.ItemStatus.approved
            AND (:type IS NULL OR i.type = :type)
            AND (:query = '' OR LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(i.location) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')))
            ORDER BY i.createdAt DESC
            """)
    List<Item> searchApproved(@Param("type") com.gecm.lostfound.model.ItemType type,
                              @Param("query") String query);
}
