package nt.faceauth.controller;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nt.faceauth.service.ClaimsListService;
import nt.faceauth.service.RegistrationService;
import nt.faceauth.to.Claim;

@Controller
@RequestMapping("/")
public class RegisterController {

	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	ClaimsListService claimsListService;
	
	@Autowired
	ServletContext applicationContext;

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView claimLoad(@ModelAttribute("claim")Claim claim) {
		return new ModelAndView("createClaim");
	}
	
	
	@RequestMapping(value="/processRekognitionOnClaimUsers", method = RequestMethod.POST)
	public ModelAndView newClaim(@ModelAttribute("claim")Claim claim, HttpSession session) {
		String bucket = applicationContext.getInitParameter("bucket");
		String album = applicationContext.getInitParameter("album");
		String collectionId = applicationContext.getInitParameter("collectionId");
		String matchThreshold = applicationContext.getInitParameter("matchThreshold");
		Float matchThresholdFloat = Float.parseFloat(matchThreshold);
		
		int maxFacesToReturn = Integer.parseInt(applicationContext.getInitParameter("maxFacesToReturn"));
		
		System.out.println("Pre-configured S3 bucket : " +  bucket); 
		System.out.println("Pre-configured Face Collection : " +  collectionId); 
		System.out.println("Pre-configured Face Album : " +  album); 
		System.out.println("Pre-configured face matches threshold : " +  matchThreshold); 
		System.out.println("Pre-configured maximum faces to return value : " +  maxFacesToReturn); 
		
		try {
			claim = registrationService.registerAndProcess(claim, bucket, collectionId, matchThresholdFloat, maxFacesToReturn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView("forward:/claimListView");
		
		session.setAttribute("success", "The data association for claim with ID number <b> " + claim.getClaimId() + " </b> has been executed successfully");
		
		return modelAndView;
	}



	
}
