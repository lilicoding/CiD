package lu.uni.snt.cid.toolkits;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.cid.utils.CommonUtils;

public class APILifeStudy 
{
	public static final int LATEST_API_LEVEL = 25; 
	static Map<String, APILife> apiLifeMap = new HashMap<String, APILife>();
	
	public static void main(String[] args) 
	{
		for (int level = 1; level <= LATEST_API_LEVEL; level++)
		{
			if (level == 20)
			{
				continue;
			}
			
			Set<String> methods = CommonUtils.loadFile("res/android-apis-refinement/android-" + level + ".txt");
			
			for (String method : methods)
			{
				if (apiLifeMap.containsKey(method))
				{
					APILife apiLife = apiLifeMap.get(method);
					if (apiLife.minAPILevel > level)
					{
						apiLife.minAPILevel = level;
					}
					if (apiLife.maxAPILevel < level)
					{
						apiLife.maxAPILevel = level;
					}
					
					apiLifeMap.put(method, apiLife);
				}
				else
				{
					APILife apiLife = new APILife();
					apiLife.signature = method;
					
					if (apiLife.minAPILevel > level)
					{
						apiLife.minAPILevel = level;
					}
					if (apiLife.maxAPILevel < level)
					{
						apiLife.maxAPILevel = level;
					}
					
					apiLifeMap.put(method, apiLife);
				}
			}
		}
		
		StringBuilder output = new StringBuilder();
		
		for (String key : apiLifeMap.keySet())
		{
			output.append(apiLifeMap.get(key) + "\n");
		}
		
		CommonUtils.writeResultToFile("res/android_api_lifetime.txt", output.toString());
	}

	static class APILife
	{
		String signature = "";
		int minAPILevel = Integer.MAX_VALUE;
		int maxAPILevel = Integer.MIN_VALUE;
		
		@Override
		public String toString()
		{
			return signature + ":[" + minAPILevel + "," + maxAPILevel + "]";
		}
	}
}
