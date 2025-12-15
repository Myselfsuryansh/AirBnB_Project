package com.codingshuttle.projects.airBnbApp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.entity.Room;
import com.codingshuttle.projects.airBnbApp.entity.User;
import com.codingshuttle.projects.airBnbApp.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private Hotel hotel;
    private Room room;
    private User user;
    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;
}
