package com.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.app.model.Ad;
import com.app.model.Product;
import com.app.repository.AdRepository;
import com.app.repository.ProductRepository;

@Service
public class AdService {
	
	@Autowired
	private AdRepository adRepostirory;
	
	@Autowired
	private ProductRepository productRepostirory;
	
	public Ad findAdByProductTitle(String title){		
		return adRepostirory.findByProductTitle(title);
	}
	
	public List<Ad> findAdByExample(Ad ad){
		
		List<Ad> finalAdList = new ArrayList<Ad>();
		
		Product product = ad.getProduct();
		ad.setProduct(null);
		
		Example<Ad> adExample = Example.of(ad);
		List<Ad> listAd = new ArrayList<Ad>();
		
		if(product != null) {
			
			Example<Product> productExample = Example.of(product);
			List<Product> listProduct = new ArrayList<Product>();
			
			listProduct = productRepostirory.findAll(productExample);
			listAd = adRepostirory.findAll(adExample);
			
			for(Ad i : listAd) {
				if(listProduct.contains(i.getProduct())) {
					finalAdList.add(i);
				}
			}
			
			return finalAdList;
			
		} else {
			return adRepostirory.findAll(adExample);
		}
	}
	
	@Transactional
	public Ad save(Ad ad) {
		try {
			productRepostirory.save(ad.getProduct());
			Ad newAd = adRepostirory.save(ad);
			return newAd;
		} catch(Exception ex) {
			
			System.out.print("\nclass: AdService | method: save \n" + ex.toString());
			
			return null;
		}
	}
	
}
