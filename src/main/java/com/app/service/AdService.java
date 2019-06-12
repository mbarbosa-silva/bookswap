package com.app.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.app.model.Ad;
import com.app.model.Address;
import com.app.model.Product;
import com.app.model.User;
import com.app.repository.AdRepository;
import com.app.repository.ProductRepository;
import com.app.repository.UserRepository;

@Service
public class AdService {
	
	@Autowired
	private AdRepository adRepostirory;
	
	@Autowired
	private ProductRepository productRepostirory;
	
	@Autowired
	private UserRepository userRepository;
		
	public Ad findAdByProductTitle(String title){		
		return adRepostirory.findByProductTitle(title);
	}
	
	public Ad findAdById(String id) {
		return adRepostirory.findAdById(Long.valueOf(id));
	}
	
	public void checkIfAdBelongsToUser(String id, User user) throws Exception {
		if(findAdById(id).getUser().getId() != user.getId()) {
			throw new Exception("ad does not belong to the user");
		} 	
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
	
	public void delete(Ad ad) {
		try {
			productRepostirory.delete(ad.getProduct());
			adRepostirory.delete(ad);
		} catch(Exception ex) {
			
		}
	}
	
    public List<Ad> findAdByUserName(String username) throws UsernameNotFoundException{
    	User user = userRepository.findByUsername(username);
    	return user.getAd();
    }
	
    public void updateAd(String id,Map<Object, Object> fields) throws Exception{
    	try {
    		
    		var ad = findAdById(id);
    		
    		fields.forEach((k, v) -> {
    			
    			if(v.getClass() == LinkedHashMap.class && k == "product") {
    				((HashMap<Object, Object>) v).forEach((t,y)->{
    					Field field = ReflectionUtils.findField(Product.class, (String) t);
    					field.setAccessible(true);
    					ReflectionUtils.setField(field,ad.getProduct(), y );
    				});
    				
    			} else {
    				Field field = ReflectionUtils.findField(Ad.class, (String) k);
					field.setAccessible(true);
					ReflectionUtils.setField(field, ad, v);
    			}
    	    });
    	
    		save(ad);
    		
    	} catch(Exception ex) {
    	}
    }
    	
    
}
