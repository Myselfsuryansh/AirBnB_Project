package com.codingshuttle.projects.airBnbApp.serviceInterface;

import org.springframework.data.domain.Page;

import com.codingshuttle.projects.airBnbApp.dto.HotelDto;
import com.codingshuttle.projects.airBnbApp.dto.HotelSearchRequest;
import com.codingshuttle.projects.airBnbApp.entity.Room;

public interface IInventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelDto> searchHotel(HotelSearchRequest hotelSearchRequest);
}
