package com.codingshuttle.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codingshuttle.projects.airBnbApp.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest,Long> {

}
