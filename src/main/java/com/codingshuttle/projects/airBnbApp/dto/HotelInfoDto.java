package com.codingshuttle.projects.airBnbApp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDto {
    private HotelDto hotel;

    private List<RoomDto> rooms;
}
