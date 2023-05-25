package com.api.parkingcontrol.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.ParkingSpotModel;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID>{
	
	boolean existsByParkingSpotNumber(String parkingSpotNumber);
	
	boolean existsByLicensePlateCar(String licensePlateCar);
	
	boolean existsByApartmentAndBlock(String apartment, String block);
	
	Optional<ParkingSpotModel> findByParkingSpotNumber(String parkingSpotNumber);
	
	Optional<ParkingSpotModel> findByLicensePlateCar(String plate);
	
	Optional<ParkingSpotModel> findByApartmentAndBlock(String apartment, String block);
	
}
