package lu.uni.snt.mining4u.toolkits;

import java.io.File;
import java.util.Set;

import lu.uni.snt.mining4u.methodextractor.FrameworkBase;
import lu.uni.snt.mining4u.utils.CommonUtils;
import lu.uni.snt.mining4u.utils.PathUtils;

public class AndroidAPIRefinement 
{
	public static void main(String[] args)
	{
		File androidAPIsDir = new File("/Users/li.li/Project_Papers/Mining4U/res/android-apis");
		for (File file : androidAPIsDir.listFiles())
		{
			FrameworkBase fb = new FrameworkBase();
			
			fb.load(file.getAbsolutePath());
			
			StringBuilder sb = new StringBuilder();
			
			for (String cls : fb.class2SuperClasses.keySet())
			{
				if (null == fb.class2SuperClasses.get(cls))
				{
					System.out.println(cls);
				}
			}
			
			for (String cls : fb.class2Methods.keySet())
			{
				Set<String> methods = fb.class2Methods.get(cls);
				if (null != methods)
				{
					for (String method : methods)
					{
						sb.append(method + "\n");
					}
				}
				else
				{
					System.out.println(cls);
				}
					
			}
			
			CommonUtils.writeResultToFile("/Users/li.li/Project_Papers/Mining4U/res/android-apis-refinement/" + PathUtils.getFileNameWithoutExtension(file.getAbsolutePath()) + ".txt", sb.toString());
		}
	}
}
