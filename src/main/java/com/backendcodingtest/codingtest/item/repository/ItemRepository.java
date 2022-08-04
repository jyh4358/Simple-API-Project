package com.backendcodingtest.codingtest.item.repository;

import com.backendcodingtest.codingtest.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);
}
