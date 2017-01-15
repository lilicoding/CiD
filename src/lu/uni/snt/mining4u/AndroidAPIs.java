package lu.uni.snt.mining4u;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.api.APILife;
import lu.uni.snt.mining4u.utils.CommonUtils;

public class AndroidAPIs 
{
	private static AndroidAPIs instance;
	
	private Map<String, APILife> androidAPIs = null;
	
	public static AndroidAPIs getInstance()
	{
		if (null == instance)
		{
			instance = new AndroidAPIs();
		}
		
		return instance;
	}
	
	public boolean isAndroidAPI(String methodSig)
	{
		if (null == androidAPIs)
		{
			androidAPIs = new HashMap<String, APILife>();
			
			Set<String> lines = CommonUtils.loadFile("res/android_api_life.txt");
			for (String line : lines)
			{
				String signature = line.substring(0, line.lastIndexOf(':'));
				String minMax = line.substring(1+line.lastIndexOf('['), line.lastIndexOf(']'));
				
				int min = Integer.parseInt(minMax.split(",")[0].trim());
				int max = Integer.parseInt(minMax.split(",")[1].trim());
				
				APILife apiLife = new APILife(signature, min, max);
				
				androidAPIs.put(signature, apiLife);
			}
		}
		
		if (androidAPIs.containsKey(methodSig))
		{
			return true;
		}
		
		return false;
	}
	
	public APILife getLifetime(String methodSig)
	{
		if (androidAPIs.containsKey(methodSig))
		{
			return androidAPIs.get(methodSig);
		}
		else
		{
			return null;
		}
	}
}
