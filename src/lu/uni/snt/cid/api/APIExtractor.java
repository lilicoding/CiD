package lu.uni.snt.cid.api;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.cid.utils.CommonUtils;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class APIExtractor 
{
	public Set<String> primaryAPIs = new HashSet<String>();
	public Set<String> secondaryAPIs = new HashSet<String>();
	
	public Map<String, Set<String>> api2callers = new HashMap<String, Set<String>>();
	public Set<String> usedAndroidAPIs = new HashSet<String>();
	
	public void transform(String apkOrDexPath, String androidJars, int apiLevel)
	{
		G.reset();
		
		String[] args =
        {
			"-process-dir", apkOrDexPath,
            "-ire",
			"-pp",
			"-keep-line-number",
			"-allow-phantom-refs",
			"-w",
			"-p", "cg", "enabled:false",
			"-src-prec", "apk"
        };
			
		Options.v().set_output_format(Options.output_format_none);
		if (-1 != apiLevel)
			Options.v().set_force_android_jar(androidJars + File.separator + "android-" + apiLevel + File.separator + "android.jar");
		else
			Options.v().set_android_jars(androidJars);
		
		Mining4UTransformer transformer = new Mining4UTransformer();
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.Mining4UTransformer", transformer));
		soot.Main.main(args);
		
		if (apkOrDexPath.endsWith(".apk"))
		{
			primaryAPIs.addAll(transformer.accessedAndroidAPIs);
			usedAndroidAPIs.addAll(primaryAPIs);
			CommonUtils.put(api2callers, transformer.api2callers);
		}
		else
		{
			secondaryAPIs.addAll(transformer.accessedAndroidAPIs);
			usedAndroidAPIs.addAll(secondaryAPIs);
			CommonUtils.put(api2callers, transformer.api2callers);
		}
		
		G.reset();
	}
}
