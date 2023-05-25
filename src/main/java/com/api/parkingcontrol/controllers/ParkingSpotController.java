package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.SpotService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

	final SpotService parkingSpotService;
	
	public ParkingSpotController(SpotService parkingSpotService) {
	
		this.parkingSpotService = parkingSpotService;
	
	}
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
		if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: This Parking Number already exists!");
		}
		if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: This License Plate already in use!");
		}
		if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: This Apartment and Block already registered!");
		}
		
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
	    return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
	}
	
	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpot
	(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
		
	}
	
	@GetMapping(value = "/parkingSpot/{number}")
	public ResponseEntity<Object> getByParkingSpotNumber(@PathVariable(value = "number") String number){
		Optional<ParkingSpotModel> parkingSpot = parkingSpotService.findByParkingSpotNumber(number);
		
		if (!parkingSpot.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Parking Number was not found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpot.get());
		
	}
	
	@GetMapping(value = "/licenseplate/{plate}")
	public ResponseEntity<Object> getByLicensePlateCar(@PathVariable(value = "plate") String plate){
		Optional<ParkingSpotModel> parkingSpot = parkingSpotService.findByLicensePlateCar(plate);
		
		if (plate.length() == 7) {
			if (!parkingSpot.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This car plate was not found!");
			}
		}else {
			
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("The license plate does not have the seven-character pattern!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpot.get());
		
	}
	
	@GetMapping(value = "/{apartment}/{block}")
	public ResponseEntity<Object> getByApartmentAndBlock(@PathVariable(value = "apartment") String apartment, 
			@PathVariable(value = "block") String block){
		
		Optional<ParkingSpotModel> parkingSpot = parkingSpotService.findByAparmentAndBlock(apartment, block);
		
		if (!parkingSpot.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Apartment or Block was not found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpot.get());
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
		Optional<ParkingSpotModel> parkingSpot = parkingSpotService.findById(id);
		
		if (!parkingSpot.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
		}
		
		parkingSpotService.delete(id);
		
		return ResponseEntity.status(HttpStatus.OK).body("The parking record has been successfully deleted!");
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
			@RequestBody @Valid ParkingSpotDto dto) throws Exception{
		ParkingSpotModel parkingSpot = parkingSpotService.update(id, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpot);
	}
}