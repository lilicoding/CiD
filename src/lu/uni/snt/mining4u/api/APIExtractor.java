package lu.uni.snt.mining4u.api;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class APIExtractor 
{
	public Set<String> primaryAPIs = new HashSet<String>();
	public Set<String> secondaryAPIs = new HashSet<String>();

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
			"-p", "wjtp.rdc", "enabled:true",
			"-src-prec", "apk"
        };
			
		Options.v().set_output_format(Options.output_format_none);
		if (-1 != apiLevel)
			Options.v().set_force_android_jar(androidJars + File.separator + "android-" + apiLevel + File.separator + "android.jar");
		else
			Options.v().set_android_jars(androidJars);
		
		APITransformer transformer = new APITransformer();
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.APITransformer", transformer));
		soot.Main.main(args);
		
		if (apkOrDexPath.endsWith(".apk"))
		{
			primaryAPIs.addAll(transformer.accessedAndroidAPIs);
		}
		else
		{
			secondaryAPIs.addAll(transformer.accessedAndroidAPIs);
		}
		
		G.reset();
	}
}
