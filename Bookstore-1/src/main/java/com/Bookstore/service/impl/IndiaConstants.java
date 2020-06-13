package com.Bookstore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndiaConstants
{
  public final static String INDIA ="India";
  public final static Map<String, String> mapOfINDIAStates = new HashMap<String, String>(){
	  {
		  put("BH","Bihar");
		  put("JH","Jharkhand");
		  put("AS","Assam");
		  put("AP","Andhra Pradesh");
		  put("CH","Chandigarh");
		  put("ND","New Delhi");
		  put("G","Goa");
		  put("GT","Gujarat");
		  put("HR","Haryana");
		  put("HP","Himachal Pradesh");
		  put("JK","Jammu and Kashmir");
		  put("K","	Karnataka");
		  put("KL","Kerala");
		  put("LD","Ladakh");
		  put("LDP","Lakshadweep");
		  put("MTR","Maharashtra");
		  put("M","Manipur");
		  put("MG","Meghalaya");
		  put("MZ","Mizoram");
		  put("NG","Nagaland");
		  put("O","	Odisha");
		  put("PD","Puducherry");
		  put("PB","Punjab");
		  put("RJ","Rajasthan");
		  put("SK","Sikkim");
		  put("TN","Tamil Nadu");
		  put("T","Telangana");
		  put("TR","Tripura");
		  put("UP","Uttar Pradesh");
		  put("U","Uttarakhand");
		  put("WB","West Bengal");
	  }
  };
  public final static List<String> listOfINDIAStatesCode = new ArrayList<String>(mapOfINDIAStates.keySet());
  public final static List<String> listOfINDIAStatesName = new ArrayList<String>(mapOfINDIAStates.values());

}
