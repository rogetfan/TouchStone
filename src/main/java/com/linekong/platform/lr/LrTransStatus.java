package com.linekong.platform.lr;

import lrapi.lr;

public class LrTransStatus 
{
	 protected String transactionName;
	 protected Double duration;
	 protected Integer status;
     public LrTransStatus(String transactionName,Double duration,Boolean passed)
     {
    	  this.transactionName = transactionName;
    	  this.duration = duration;
    	  this.status = passed ? lr.PASS:lr.FAIL;
     }
}
