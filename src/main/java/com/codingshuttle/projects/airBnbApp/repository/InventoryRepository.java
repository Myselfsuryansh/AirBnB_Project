package com.codingshuttle.projects.airBnbApp.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.entity.Inventory;
import com.codingshuttle.projects.airBnbApp.entity.Room;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByRoom(Room room);

    @Query("""
                SELECT i.hotel
                FROM Inventory i
                WHERE LOWER(i.city) = LOWER(:city)
                  AND i.date BETWEEN :startDate AND :endDate
                  AND i.closed = false
                GROUP BY i.hotel
                HAVING COUNT(DISTINCT i.date) = :dateCount
                   AND SUM(i.totalCount - i.bookedCount) >= :numberOfRooms
            """)
    Page<Hotel> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") Long numberOfRooms,
            @Param("dateCount") Long dateCount,
            Pageable pageable);

}
