package org.elise.test.lr;

import lrapi.lr;

public class LrTransStatus
{
	public String transactionName;
	public Double duration;
	public Integer status;
	public LrTransStatus(String transactionName,Double duration,Boolean passed)
	{
    	  this.transactionName = transactionName;
    	  this.duration = duration;
    	  this.status = passed ? lr.PASS:lr.FAIL;
	}
}
