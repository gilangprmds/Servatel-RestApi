package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);

    Page<Hotel> findAllByUser_Id(Pageable pageable, Long id);

    @Query(value = "SELECT h " +
            "FROM Hotel h " +
            "JOIN h.rooms r " +
            "LEFT JOIN Availability a ON a.room.id = r.id " +
            "AND a.date >= :checkinDate AND a.date < :checkoutDate " +
            "WHERE h.address.city = :city " +
            "AND (a IS NULL OR a.availableRooms > 0) " +
            "GROUP BY h.id, h.name,h.address.id, h.user.id, h.description, r.id " +
            "HAVING COUNT(DISTINCT a.date) + SUM(CASE WHEN a IS NULL THEN 1 ELSE 0 END) = :numberOfDays")
    Page<Hotel> findHotelsWithAvailableRooms(Pageable pageable,
                                             @Param("city") String city,
                                             @Param("checkinDate") LocalDate checkinDate,
                                             @Param("checkoutDate") LocalDate checkoutDate,
                                             @Param("numberOfDays") Long numberOfDays);

    @Query("SELECT h " +
            "FROM Hotel h " +
            "WHERE h.address.city = :city " +
            "AND NOT EXISTS (" +
            "   SELECT 1 " +
            "   FROM Availability a " +
            "   WHERE a.room.hotel.id = h.id " +
            "   AND a.date >= :checkinDate AND a.date < :checkoutDate" +
            ")")
    Page<Hotel> findHotelsWithoutAvailabilityRecords(Pageable pageable,
                                                     @Param("city") String city,
                                                     @Param("checkinDate") LocalDate checkinDate,
                                                     @Param("checkoutDate") LocalDate checkoutDate);

    @Query("SELECT h " +
            "FROM Hotel h " +
            "JOIN h.rooms r " +
            "LEFT JOIN Availability a ON r.id = a.room.id " +
            "AND a.date >= :checkinDate AND a.date < :checkoutDate " +
            "WHERE h.address.city = :city " +
            "AND (a IS NULL OR a.availableRooms > 0) " +
            "GROUP BY h.id, h.name, h.address.id, h.user.id, h.description " +
            "HAVING COUNT(DISTINCT a.date) < :numberOfDays " +
            "AND COUNT(CASE WHEN a.availableRooms > 0 THEN a.date END) > 0")
    Page<Hotel> findHotelsWithPartialAvailabilityRecords(Pageable pageable,
                                                         @Param("city") String city,
                                                         @Param("checkinDate") LocalDate checkinDate,
                                                         @Param("checkoutDate") LocalDate checkoutDate,
                                                         @Param("numberOfDays") Long numberOfDays);
}
