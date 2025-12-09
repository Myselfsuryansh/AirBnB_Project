package com.codingshuttle.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingshuttle.projects.airBnbApp.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long>{

}
