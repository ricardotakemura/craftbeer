package com.beerhouse.service;

import java.util.List;

import com.beerhouse.dto.BeerDTO;

public interface BeerService {

	List<BeerDTO> getBeers();

	BeerDTO getBeer(Integer id);
	
	BeerDTO getBeerByName(String name);

	Integer saveBeer(BeerDTO beer);

	void deleteBeer(Integer id);

}
