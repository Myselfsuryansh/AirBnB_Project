package com.codingshuttle.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingshuttle.projects.airBnbApp.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
