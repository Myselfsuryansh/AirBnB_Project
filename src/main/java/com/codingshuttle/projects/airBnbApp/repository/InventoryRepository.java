package com.codingshuttle.projects.airBnbApp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codingshuttle.projects.airBnbApp.entity.Hotel;
import com.codingshuttle.projects.airBnbApp.entity.Inventory;
import com.codingshuttle.projects.airBnbApp.entity.Room;

import jakarta.persistence.LockModeType;

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
             AND SUM(i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
      """)
  Page<Hotel> findHotelWithAvailableInventory(
      @Param("city") String city,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("numberOfRooms") Long numberOfRooms,
      @Param("dateCount") Long dateCount,
      Pageable pageable);

  @Query("""
      SELECT i
      FROM Inventory i
      WHERE i.room.id =:roomId
        AND i.date BETWEEN :startDate AND :endDate
        AND i.closed = false
        AND (i.totalCount - i.bookedCount - i.reservedCount) >:roomsCount
      """)
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Inventory> findAndLockAvailableInventory(
      @Param("roomId") Long roomId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("roomsCount") Integer roomsCount);

}
