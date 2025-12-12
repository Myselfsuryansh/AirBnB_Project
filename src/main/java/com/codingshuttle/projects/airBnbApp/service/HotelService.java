package com.codingshuttle.projects.airBnbApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.codingshuttle.projects.airBnbApp.dto.HotelDto;
import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.projects.airBnbApp.repository.HotelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
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
    public void deleteHotelById(Long id) {
        boolean isExits = isExitsByHotelId(id);
        if (!isExits) {
            throw new ResourceNotFoundException("Hotel Not Found with Id :" + id);
        }
        hotelRepository.deleteById(id);
        // ToDo: delete the future inventories for the Hotel
    }

     @Override
    public void activateHotel(Long hotelId) {
        log.info("Activating the Hotel with the Id:{} :", +hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id" + hotelId));
        hotel.setActive(true);
    }

    public boolean isExitsByHotelId(Long id) {
        boolean isExits = hotelRepository.existsById(id);
        if (!isExits) {
            throw new ResourceNotFoundException("Hotel Not Found with Id :" + id);
        }
        return true;
    }

   

}
