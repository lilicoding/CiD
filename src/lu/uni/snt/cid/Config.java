package lu.uni.snt.cid;

public class Config 
{
	public static final int LATEST_API_LEVEL = 25;
	public static final int DEFAULT_API_LEVEL = 19;
	
	public static final String FIELD_VERSION_SDK_INT = "<android.os.Build$VERSION: int SDK_INT>";
	public static final String FIELD_VERSION_SDK = "<android.os.Build$VERSION: java.lang.String SDK>";
	
	public static String apkPath = "";
	public static String apkName = "";
	public static String androidJars = "";
	
	
	public static boolean DEBUG = false;
	public static boolean containsSDKVersionChecker = false;
	//public static ConditionalCG ccg = new ConditionalCG();
}
