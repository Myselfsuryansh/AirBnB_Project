package com.codingshuttle.projects.airBnbApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.codingshuttle.projects.airBnbApp.dto.RoomDto;
import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.entity.Room;
import com.codingshuttle.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.projects.airBnbApp.repository.HotelRepository;
import com.codingshuttle.projects.airBnbApp.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new Room with id : {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        // Create inventory as soon as room is Created and if hotel is active
        if(hotel.getActive())
        {
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoom(Long hotelId) {
        log.info("Getting all Room data");
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + hotelId));

        return hotel.getRooms().stream().map((ele) -> modelMapper.map(ele, RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long id) {
        log.info("Getting the Room with Id :" + id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + id));
        return modelMapper.map(room, RoomDto.class);
    }
    @Transactional
    @Override
    public void deleteRoomById(Long id) {
        log.info("Deleting the room by id:" +id);
       Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + id));
        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(id);
    }

    public boolean isExitsByRoomId(Long id) {
        boolean isExits = roomRepository.existsById(id);
        if (!isExits) {
            throw new ResourceNotFoundException("Room Not Found with Id :" + id);
        }
        return true;
    }

}
