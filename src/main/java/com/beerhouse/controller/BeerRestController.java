package com.beerhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beerhouse.business.BeerBusiness;
import com.beerhouse.dto.BeerDTO;
import com.beerhouse.exception.DataAlreadyExistsException;
import com.beerhouse.exception.DataNotFoundException;
import com.beerhouse.exception.InvalidFieldValueException;

@RestController
public class BeerRestController {

	@Autowired
	private BeerBusiness beerBusiness;

	@RequestMapping(path = "/beers", method = RequestMethod.GET)
	public ResponseEntity<List<BeerDTO>> listBeers() {
		List<BeerDTO> beers = beerBusiness.getBeers();
		return new ResponseEntity<List<BeerDTO>>(beers, HttpStatus.OK);
	}

	@RequestMapping(path = "/beers", method = RequestMethod.POST)
	public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beerDTO) {
		try {
			beerBusiness.createBeer(beerDTO);
			return new ResponseEntity<BeerDTO>(HttpStatus.CREATED);
		} catch (DataAlreadyExistsException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.BAD_REQUEST);
		} catch (InvalidFieldValueException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(path = "/beer/{id}", method = RequestMethod.GET)
	public ResponseEntity<BeerDTO> getBeer(@PathVariable("id") Integer id) {
		try {
			BeerDTO beer = beerBusiness.getBeer(id);
			return new ResponseEntity<BeerDTO>(beer, HttpStatus.OK);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/beer/{id}", method = RequestMethod.PUT)
	public ResponseEntity<BeerDTO> updateBeer(@PathVariable("id") Integer id, @RequestBody BeerDTO beerDTO) {
		try {
			beerBusiness.updateBeer(id, beerDTO);
			return new ResponseEntity<BeerDTO>(HttpStatus.OK);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.NOT_FOUND);
		} catch (InvalidFieldValueException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(path = "/beer/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<BeerDTO> changeBeer(@PathVariable("id") Integer id, @RequestBody BeerDTO beerDTO) {
		try {
			beerBusiness.changeBeer(id, beerDTO);
			return new ResponseEntity<BeerDTO>(HttpStatus.OK);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<BeerDTO>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/beer/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<BeerDTO> deleteBeer(@PathVariable("id") Integer id) {
		beerBusiness.deleteBeer(id);
		return new ResponseEntity<BeerDTO>(HttpStatus.NO_CONTENT);
	}
}
