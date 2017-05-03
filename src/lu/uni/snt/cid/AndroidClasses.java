package lu.uni.snt.cid;
import java.util.Set;

import lu.uni.snt.cid.utils.CommonUtils;

public class AndroidClasses 
{
	static Set<String> androidClasses = null;
	
	public static boolean isAndroidClass(String cls)
	{
		if (null == androidClasses)
		{
			androidClasses = CommonUtils.loadFile("res/android-classes.txt");
		}
		
		if (androidClasses.contains(cls))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
