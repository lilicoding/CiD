package lu.uni.snt.cid.toolkits;

import java.io.File;
import java.util.Set;

import lu.uni.snt.cid.FrameworkBase;
import lu.uni.snt.cid.utils.CommonUtils;
import lu.uni.snt.cid.utils.MethodSignature;
import lu.uni.snt.cid.utils.PathUtils;

public class AndroidAPIRefinement 
{
	public static void main(String[] args)
	{
		File androidAPIsDir = new File("/Users/li.li/Documents/workspace2016/Mining4U/res/android-apis");
		for (File file : androidAPIsDir.listFiles())
		{
			if (! (file.getName().endsWith(".txt") || file.getName().endsWith(".xml")))
			{
				continue;
			}
			
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
						method = new MethodSignature(method).getSignatureWithoutGPItems();
						sb.append(method + "\n");
					}
				}
				else
				{
					System.out.println(cls);
				}

			}
			
			CommonUtils.writeResultToFile("/Users/li.li/Documents/workspace2016/Mining4U/res/android-apis-refinement/" + PathUtils.getFileNameWithoutExtension(file.getAbsolutePath()) + ".txt", sb.toString());
		}
	}
}
