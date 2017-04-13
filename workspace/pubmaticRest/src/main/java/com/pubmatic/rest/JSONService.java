package com.pubmatic.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/json/product")
public class JSONService {

	@POST
	@Path("/get")
	@Produces("application/json")
	public Map<String,String> getProductInJSON(@QueryParam("input") String input,List<String> method) {
		Map<String,String> map = new HashMap<String, String>();
		try{
			if(null != method && method.size() > 0){
				for(String algorthim : method){
					if(algorthim.equalsIgnoreCase("MD5"))
						map.put("MD5", getMD5(input));
					else if(algorthim.equalsIgnoreCase("CRC32"))
						map.put("CRC32", getCRC32(input));
					else if(algorthim.equalsIgnoreCase("CRC64")){
						Long log = Crc64.generate(input);
						map.put("CRC64", log.toString());
						
					}
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return map;

	}
	
	private String getMD5(String input){
		MessageDigest md = null;
		try {
			
			md = MessageDigest.getInstance("MD5");
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        md.update(input.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
    }
	
	private String getCRC32(String input){
		byte bytes[] = input.getBytes();
        
        Checksum checksum = new CRC32();
        checksum.update(bytes,0,bytes.length);
        Long lngChecksum = checksum.getValue();
        return lngChecksum.toString();
	}
	
}

