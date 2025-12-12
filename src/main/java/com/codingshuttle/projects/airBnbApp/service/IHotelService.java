package com.codingshuttle.projects.airBnbApp.service;

import java.util.List;

import com.codingshuttle.projects.airBnbApp.dto.HotelDto;

public interface IHotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    List<HotelDto> getAllHotel();

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);
}
