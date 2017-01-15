package lu.uni.snt.mining4u;

import java.util.HashSet;
import java.util.Set;

import lu.uni.snt.mining4u.utils.CommonUtils;

public class AndroidClasses 
{
	private static Set<String> androidClasses = null;
	
	public static boolean isAndroidClass(String clsName)
	{
		if (null == androidClasses)
		{
			androidClasses = new HashSet<String>();
			Set<String> lines = CommonUtils.loadFile("res/android_api_life.txt");
			for (String line : lines)
			{
				String str = line.substring(1, line.indexOf(':'));
				androidClasses.add(str);
			}
		}
		
		if (androidClasses.contains(clsName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
