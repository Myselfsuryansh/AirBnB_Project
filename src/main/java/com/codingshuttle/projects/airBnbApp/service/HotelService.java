package com.codingshuttle.projects.airBnbApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.codingshuttle.projects.airBnbApp.dto.HotelDto;
import com.codingshuttle.projects.airBnbApp.dto.HotelInfoDto;
import com.codingshuttle.projects.airBnbApp.dto.RoomDto;
import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.entity.Room;
import com.codingshuttle.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.projects.airBnbApp.repository.HotelRepository;
import com.codingshuttle.projects.airBnbApp.repository.RoomRepository;
import com.codingshuttle.projects.airBnbApp.serviceInterface.IHotelService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name : {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with Id : {}", hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with Id :" + id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + id));
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public List<HotelDto> getAllHotel() {
        log.info("Getting all Hotel data");
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream().map(h -> modelMapper.map(h, HotelDto.class)).collect(Collectors.toList());
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with Id and data:" + id + " " + hotelDto.getName());
        boolean isExits = isExitsByHotelId(id);
        if (!isExits) {
            throw new ResourceNotFoundException("Employee Not Found with Id :" + id);
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + id));

        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + id));
        // ToDo: delete the future inventories for the Hotel
        for (Room room : hotel.getRooms()) {
            inventoryService.deleteAllInventories(room);
            roomRepository.delete(room);
        }
        hotelRepository.delete(hotel);

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the Hotel with the Id:{} :", +hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + hotelId));
        hotel.setActive(true);
        // Assuming get it only once inventories for the Hotel:

        for (Room room : hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }
    }

    public boolean isExitsByHotelId(Long id) {
        boolean isExits = hotelRepository.existsById(id);
        if (!isExits) {
            throw new ResourceNotFoundException("Hotel Not Found with Id :" + id);
        }
        return true;
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + hotelId));

        List<RoomDto> roomDtos = hotel.getRooms().stream().map((ele) -> modelMapper.map(ele, RoomDto.class)).toList();
        
        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class),roomDtos);
    }

}
