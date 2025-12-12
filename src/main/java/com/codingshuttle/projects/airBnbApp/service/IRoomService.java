package com.codingshuttle.projects.airBnbApp.service;

import java.util.List;

import com.codingshuttle.projects.airBnbApp.dto.RoomDto;

public interface IRoomService {
    RoomDto createNewRoom(Long hotelId,RoomDto roomDto);

    List<RoomDto> getAllRoom(Long hotelId);

    RoomDto getRoomById(Long id);

    void deleteRoomById(Long id);
}
