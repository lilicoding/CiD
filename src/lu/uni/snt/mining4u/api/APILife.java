package lu.uni.snt.mining4u.api;

import java.io.Serializable;

public class APILife implements Serializable
{
	private static final long serialVersionUID = 2736868985941458468L;
	
	String signature = "";
	int minAPILevel = Integer.MAX_VALUE;
	int maxAPILevel = Integer.MIN_VALUE;
	
	public APILife() {}
	
	public APILife(String signature, int min, int max) 
	{
		this.signature = signature;
		this.minAPILevel = min;
		this.maxAPILevel = max;
	}
	
	public APILife(String apiLifeTxt)
	{
		this.signature = apiLifeTxt.substring(0, apiLifeTxt.lastIndexOf(':'));
		
		String levelStr = apiLifeTxt.substring(apiLifeTxt.lastIndexOf('[') + 1, apiLifeTxt.lastIndexOf(']'));
		this.minAPILevel = Integer.parseInt(levelStr.split(",")[0]);
		this.maxAPILevel = Integer.parseInt(levelStr.split(",")[1]);
	}

	@Override
	public String toString()
	{
		return signature + ":[" + minAPILevel + "," + maxAPILevel + "]";
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getMinAPILevel() {
		return minAPILevel;
	}

	public void setMinAPILevel(int minAPILevel) {
		this.minAPILevel = minAPILevel;
	}

	public int getMaxAPILevel() {
		return maxAPILevel;
	}

	public void setMaxAPILevel(int maxAPILevel) {
		this.maxAPILevel = maxAPILevel;
	}
}
