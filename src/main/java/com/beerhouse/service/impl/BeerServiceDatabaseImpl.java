package com.beerhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.beerhouse.dto.BeerDTO;
import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.service.BeerService;

@Service
public class BeerServiceDatabaseImpl implements BeerService {

	private static final Log log = LogFactory.getLog(BeerServiceDatabaseImpl.class);

	@Autowired
	private BeerRepository beerRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<BeerDTO> getBeers() {
		log.debug("[START] getBeers: List<BeerDTO>");
		List<Beer> beers = beerRepository.findAll();
		List<BeerDTO> result = new ArrayList<BeerDTO>();
		for (Beer beer : beers) {
			BeerDTO item = new BeerDTO();
			BeanUtils.copyProperties(beer, item);
			result.add(item);
		}
		log.debug("[END] getBeers: List<BeerDTO> -> " + result);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public BeerDTO getBeer(Integer id) {
		log.debug("[START] getBeer(" + id + "): BeerDTO");
		BeerDTO result = null;
		try {
			Beer beer = beerRepository.getOne(id);
			if (beer != null && beer.getId() != null) {
				result = new BeerDTO();
				BeanUtils.copyProperties(beer, result);
			}
			return result;
		} catch (EntityNotFoundException e) {
			log.error("[ERROR] Beer not found with id: " + id);
			return result;
		} finally {
			log.debug("[END] getBeer(" + id + "): BeerDTO -> " + result);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public BeerDTO getBeerByName(String name) {
		log.debug("[START] getBeerByName(" + name + "): BeerDTO");
		BeerDTO result = null;
		try {
			Beer beer = beerRepository.findByName(name);
			if (beer != null && beer.getId() != null) {
				result = new BeerDTO();
				BeanUtils.copyProperties(beer, result);
			}
			return result;
		} catch (EntityNotFoundException e) {
			log.error("[ERROR] Beer not found with id: " + name);
			return result;
		} finally {
			log.debug("[END] getBeerByName(" + name + "): BeerDTO -> " + result);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer saveBeer(BeerDTO beer) {
		log.debug("[START] saveBeer(" + beer + "): Integer");
		Beer data = new Beer();
		BeanUtils.copyProperties(beer, data);
		data = beerRepository.saveAndFlush(data);
		log.debug("[END] saveBeer(" + beer + "): Integer -> " + data.getId());
		return data.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBeer(Integer id) {
		log.debug("[START] deleteBeer(" + id + "): Void");
		beerRepository.delete(id);
		log.debug("[END] deleteBeer(" + id + "): Void");
	}

}
