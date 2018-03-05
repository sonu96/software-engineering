package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Controller
public class ProfileController {
    @GetMapping(value ="/")
    public ModelAndView renderpage() {
    	
    	    ModelAndView indexPage = new ModelAndView();
    	    
      	indexPage.setViewName("index");
    	    return indexPage;
    }
    @Value("${accesskey1}")
    String accesskey1;
    @Value("${accesskey2}")
    String accesskey2;
    
    @PostMapping(value ="/upload")
    public ModelAndView uploadTos3(
    		@RequestParam("file") MultipartFile image
    		) {
    	   ModelAndView profilePage = new ModelAndView();
    	   BasicAWSCredentials cred = new BasicAWSCredentials(accesskey1,accesskey2 );
    	   AmazonS3 s3client = AmazonS3ClientBuilder
    			   .standard()
    			   .withCredentials(new AWSStaticCredentialsProvider(cred))
    			   .withRegion(Regions.US_EAST_2)
    			   .build();
    	 	try {
    	
    	   PutObjectRequest putReq = new PutObjectRequest("abhisonu",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata())
    	   .withCannedAcl(CannedAccessControlList.PublicRead);
    	   
    	   s3client.putObject(putReq);
    	   
    	   String imgsrc =  "http://"+"abhisonu"  +".s3.amazonaws.com/"+image.getOriginalFilename();
    	   profilePage.addObject("imgsrc",imgsrc);
    	   profilePage.setViewName("ProfilePage");
    	   return profilePage;
       
    }catch (IOException e) {
    	e.printStackTrace();
    	profilePage.setViewName("er");
    	return profilePage;
    }
    	    

    }  
       
       
}


