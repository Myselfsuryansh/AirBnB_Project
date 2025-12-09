package com.codingshuttle.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingshuttle.projects.airBnbApp.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

}
