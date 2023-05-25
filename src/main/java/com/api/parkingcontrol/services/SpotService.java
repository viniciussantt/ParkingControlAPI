package com.api.parkingcontrol.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

@Service
public class SpotService {
	
	final ParkingSpotRepository parkingSpotRepository;
	
	public SpotService(ParkingSpotRepository parkingSpotRepository) {
		
		this.parkingSpotRepository = parkingSpotRepository;
		
		
	}

	@Transactional
	public Object save(ParkingSpotModel parkingSpotModel) {
		
		return parkingSpotRepository.save(parkingSpotModel);
	
	}
	
	public boolean existsByLicensePlateCar(String licensePlateCar) {
		
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
		
	}
	
	public boolean existsByApartmentAndBlock(String apartment, String block) {
		
		return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
		
	}
	
	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
		
	}
	
	public Page<ParkingSpotModel> findAll(Pageable pageable){
		
		return parkingSpotRepository.findAll(pageable);
		
	}
	
	public Optional<ParkingSpotModel> findById(UUID id){
		
		return parkingSpotRepository.findById(id);
		
	}
	
	public Optional<ParkingSpotModel> findByParkingSpotNumber(String number){
		
		return parkingSpotRepository.findByParkingSpotNumber(number);
		
	}
	
	public Optional<ParkingSpotModel> findByLicensePlateCar(String plate){
		
		return parkingSpotRepository.findByLicensePlateCar(plate);
		
	}
	
	public Optional<ParkingSpotModel> findByAparmentAndBlock(String apartment, String block){
		
		return parkingSpotRepository.findByApartmentAndBlock(apartment, block);
		
	}
	
	public ParkingSpotModel update(UUID id, ParkingSpotDto dto) throws Exception {
        Optional<ParkingSpotModel> optionalParkingSpot = parkingSpotRepository.findById(id);
        
        if (optionalParkingSpot.isEmpty()) {
            throw new Exception("Parking spot not found!");
        }

        ParkingSpotModel parkingSpot = optionalParkingSpot.get();
        updateParkingSpotFromDto(dto, parkingSpot);
        return parkingSpotRepository.save(parkingSpot);
    }
	
	private void updateParkingSpotFromDto(ParkingSpotDto dto, ParkingSpotModel parkingSpot) {
		parkingSpot.setParkingSpotNumber(dto.getParkingSpotNumber());
		parkingSpot.setLicensePlateCar(dto.getLicensePlateCar());
		parkingSpot.setBrandCar(dto.getBrandCar());
		parkingSpot.setModelCar(dto.getModelCar());
		parkingSpot.setColorCar(dto.getColorCar());
		parkingSpot.setResponsibleName(dto.getResponsibleName());
		parkingSpot.setApartment(dto.getApartment());
		parkingSpot.setBlock(dto.getBlock());
	}
	
	@Transactional
	public void delete(UUID id) {
		
		parkingSpotRepository.deleteById(id);
		
	}
}
