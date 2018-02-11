package nt.faceauth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nt.faceauth.service.ClaimsFraudCheckService;
import nt.faceauth.service.ClaimsListService;
import nt.faceauth.to.Claim;
import nt.faceauth.to.ClaimsCheckResult;

@Controller
public class ClaimListViewController {
	
	@Autowired
	ClaimsListService claimsListService;

	@Autowired
	ClaimsFraudCheckService claimsFraudCheckService;

	@Autowired
	ServletContext applicationContext;
	
	@RequestMapping(value="/claimListView", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView displayList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("claim") Claim claim) {
		List<Claim> claimsListResult = claimsListService.retrieveClaims();

		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("claimsList", claimsListResult);
		modelAndView.setViewName("claimList");
		
		request.getSession().setAttribute("claimsList", claimsListResult);
				
		return modelAndView;
	}
		
	@RequestMapping(value="/compareClaim", method = RequestMethod.POST)
	public ModelAndView compareClaim(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		
		String bucket = applicationContext.getInitParameter("bucket");
		String album = applicationContext.getInitParameter("album");
		String collectionId = applicationContext.getInitParameter("collectionId");
		String matchThreshold = applicationContext.getInitParameter("matchThreshold");
		Float matchThresholdFloat = Float.parseFloat(matchThreshold);
		
		int maxFacesToReturn = Integer.parseInt(applicationContext.getInitParameter("maxFacesToReturn"));
		
/*		List<Claim> claimsListResult = claimsListService.retrieveClaims();

*/		
		List<Claim> claimsListResult = (List<Claim>)request.getAttribute("claimsList");
		
		List<Claim> claimsListCheckResult = new ArrayList<Claim>();
		
		String[] selectedClaims = request.getParameterValues("claimSelect");
		
		int[] claimIds = new int[selectedClaims.length];
		for (int i = 0; i < selectedClaims.length; i++) {
			claimIds[i] = Integer.parseInt(selectedClaims[i]);
		}
		
		claimsListCheckResult = claimsListService.retrieveClaimById(claimIds);
		
		Map<Integer, List<Integer>> claimsFraudMap = new HashMap<Integer, List<Integer>>();
		
		List<Claim> fraudClaims = claimsFraudCheckService.findFraudClaimsMatch(bucket, collectionId, matchThresholdFloat, maxFacesToReturn, claimsListCheckResult, claimsFraudMap);
		
		HttpSession session = request.getSession();

		if(null != fraudClaims) {
			session.setAttribute("alert", "Possible fraud detected! Highlighted participant(s) could be part of a "
					+ "fraud chain!");
			modelAndView.addObject("claimsCheckList", fraudClaims);
			modelAndView.addObject("claimsFraudMap", claimsFraudMap);

		} else {
			session.setAttribute("success", "Selected claims have been validated successfully. There is no indication of fraud chain involvement on selected claims.");
			modelAndView.addObject("claimsCheckList", claimsListCheckResult);
		}
		
		modelAndView.setViewName("claimListResult");
		
		return modelAndView;
	}

}
