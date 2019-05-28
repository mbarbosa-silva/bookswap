package com.app.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.app.model.Ad;
import com.app.repository.AdRepository;
import com.app.repository.ProductRepository;

@Service
public class AdService {
	
	@Autowired
	private AdRepository adRepostirory;
	
	@Autowired
	private ProductRepository productRepostirory;
	
	public Collection<Ad> findAdByProductTitle(String title){
		return adRepostirory.findByProductTitle(title);
	}
	
	public List<Ad> findAdByExample(Ad ad){
		Example<Ad> adExample = Example.of(ad);
		return adRepostirory.findAll(adExample);
	}
	
	public Ad save(Ad ad) {
		try {
			productRepostirory.save(ad.getProduct());
			Ad newAd = adRepostirory.save(ad);
			return newAd;
		} catch(Exception ex) {
			return null;
		}
	}
	
}
