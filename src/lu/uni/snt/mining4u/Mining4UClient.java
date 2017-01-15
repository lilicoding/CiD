package lu.uni.snt.mining4u;

import java.util.Set;

import lu.uni.snt.mining4u.api.APIExtractor;
import lu.uni.snt.mining4u.api.APILife;
import lu.uni.snt.mining4u.ari.AndroidSDKVersionChecker;
import lu.uni.snt.mining4u.ari.ConditionalCallGraph;
import lu.uni.snt.mining4u.dcl.DexHunter;

public class Mining4UClient 
{
	public static final int LATEST_API_LEVEL = 25;
	
	
	public static void main(String[] args) 
	{
		String apkPath = args[0];
		String androidJars = args[1];
		
		String apkName = apkPath;
		if (apkName.contains("/"))
		{
			apkName = apkName.substring(apkName.lastIndexOf('/')+1);
		}
		
		//(1) Unzip Android APK and extract all additonally accessed DEXes
		Set<String> additionalDexes = new DexHunter(apkPath).hunt();
		
		//(2) Extracting the targeted Sdk version
		AndroidManifest manifest = new AndroidManifest(apkName);
		int apiLevel = inferAPILevel(manifest);
		
		//(3) Extracting the leveraged Android APIs (primary and all)
		APIExtractor extractor = new APIExtractor();
		extractor.transform(apkPath, androidJars, apiLevel);
		System.out.println("Found " + additionalDexes.size() + " DEX files. Now visiting them one by one.");
		for (String dex : additionalDexes)
		{
			extractor.transform(dex, androidJars, apiLevel);
		}
		
		int minAPILevel = manifest.getMinSdkVersion();
		int maxAPILevel = manifest.getMaxSdkVersion();
		if (-1 == maxAPILevel)
		{
			maxAPILevel = LATEST_API_LEVEL;
		}
		
		//(4) SDK check study
		AndroidSDKVersionChecker.scan(apkPath, androidJars);
		
		System.out.println("Declared Min Sdk version is: " + manifest.getMinSdkVersion());
		System.out.println("Declared Target Sdk version is: " + manifest.getTargetSdkVersion());
		System.out.println("Declared Max Sdk version is: " + manifest.getMaxSdkVersion());
		
		for (String method : extractor.primaryAPIs)
		{
			APILife lifetime = AndroidAPILifeModel.getInstance().getLifetime(method);
			
			if (lifetime.getMinAPILevel() > minAPILevel || lifetime.getMaxAPILevel() < maxAPILevel)
			{
				System.out.println("==>" + method + "[" + lifetime.getMinAPILevel() + ", " + lifetime.getMaxAPILevel() + "]");
				System.out.println(ConditionalCallGraph.obtainConditions(method));
				System.out.println(ConditionalCallGraph.obtainCallStack(method));
			}
		}
		
		//(4) Extracting the SDK version checks
		//Map<String, APISDKCheck> method2SDKChecks = AndroidSDKVersionChecker.scan(apkPath, androidJars);
		
		/*
		String constraint4Primary = Integer.MIN_VALUE + "," + Integer.MAX_VALUE;
		String constraint4Primary2 = Integer.MIN_VALUE + "," + Integer.MAX_VALUE;
		StringBuilder content = new StringBuilder();
		for (String method : extractor.primaryAPIs)
		{
			content.append(method + "\n");
			
			APILife lifetime = AndroidAPILifeModel.getInstance().getLifetime(method);
			constraint4Primary = constraint(lifetime.getMinAPILevel(), lifetime.getMaxAPILevel(), constraint4Primary);
			
			APILife lifetime2 = AndroidAPILifeModel.getInstance().getLifetime(method, method2SDKChecks);
			constraint4Primary2 = constraint(lifetime2.getMinAPILevel(), lifetime2.getMaxAPILevel(), constraint4Primary2);
		}
		CommonUtils.writeResultToFile("mining4u_primary_apis_" + apkName + ".txt", content.toString());
		
		Set<String> allAPIs = new HashSet<String>();
		allAPIs.addAll(extractor.primaryAPIs);
		allAPIs.addAll(extractor.secondaryAPIs);
		
		String constraint4Secondary = Integer.MIN_VALUE + "," + Integer.MAX_VALUE;
		String constraint4Secondary2 = Integer.MIN_VALUE + "," + Integer.MAX_VALUE;
		
		content = new StringBuilder();
		for (String method : allAPIs)
		{
			content.append(method + "\n");
			
			APILife lifetime = AndroidAPILifeModel.getInstance().getLifetime(method);
			constraint4Secondary = constraint(lifetime.getMinAPILevel(), lifetime.getMaxAPILevel(), constraint4Secondary);
			
			APILife lifetime2 = AndroidAPILifeModel.getInstance().getLifetime(method, method2SDKChecks);
			constraint4Secondary2 = constraint(lifetime2.getMinAPILevel(), lifetime2.getMaxAPILevel(), constraint4Secondary2);
			
			System.out.println("==>" + method + ":[" + lifetime2.getMinAPILevel() + "," + lifetime2.getMaxAPILevel() + "]");
		}
		CommonUtils.writeResultToFile("mining4u_secondary_apis_" + apkName + ".txt", content.toString());
		
		System.out.println("Declared Min Sdk version is: " + manifest.getMinSdkVersion());
		System.out.println("Declared Target Sdk version is: " + manifest.getTargetSdkVersion());
		System.out.println("Declared Max Sdk version is: " + manifest.getMaxSdkVersion());
		
		System.out.println("Expected Sdk versions are (without DCL): " + constraint4Primary);
		System.out.println("Expected Sdk versions are (without DCL, With SDK Check): " + constraint4Primary2);
		System.out.println("Expected Sdk versions are (with DCL): " + constraint4Secondary);
		System.out.println("Expected Sdk versions are (with DCL, With SDK Check): " + constraint4Secondary2);
		*/
	}

	public static String constraint(int min1, int max1, int min2, int max2)
	{
		int min = min1 > min2 ? min1 : min2;
		int max = max1 > max2 ? max2 : max1;
		
		return min + "," + max;
	}
	
	public static String constraint(int min1, int max1, String minMax)
	{
		int min2 = Integer.parseInt(minMax.split(",")[0]);
		int max2 = Integer.parseInt(minMax.split(",")[1]);
		
		return constraint(min1, max1, min2, max2);
	}
	
	public static int inferAPILevel(AndroidManifest manifest)
	{
		int apiLevel = -1;
		if (-1 != manifest.getTargetSdkVersion())
		{
			apiLevel = manifest.getTargetSdkVersion();
		}
		else if (-1 != manifest.getMaxSdkVersion())
		{
			apiLevel = manifest.getMaxSdkVersion();
		}
		else if (-1 != manifest.getMinSdkVersion())
		{
			apiLevel = manifest.getMinSdkVersion();
		}
		
		return apiLevel;
	}
}
