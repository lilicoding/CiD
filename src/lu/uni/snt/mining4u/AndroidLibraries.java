package lu.uni.snt.mining4u;
import java.util.Set;

import lu.uni.snt.mining4u.utils.CommonUtils;

public class AndroidLibraries 
{
	static Set<String> androidLibraries = null;
	
	public static boolean isAndroidLibrary(String cls)
	{
		if (null == androidLibraries)
		{
			androidLibraries = CommonUtils.loadFile("res/android-libraries.txt");
		}
		
		for (String prefix : androidLibraries)
		{
			if (cls.startsWith(prefix))
			{
				return true;
			}
		}
		
		return false;
	}
}
