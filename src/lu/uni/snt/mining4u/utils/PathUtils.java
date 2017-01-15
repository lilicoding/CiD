package lu.uni.snt.mining4u.utils;

public class PathUtils 
{
	public static void main(String[] args)
	{
		System.out.println(getFileName("/Users/li.li/Project_Papers/Mining4U/res/android-apis/android-1.xml"));
		System.out.println(getFileNameWithoutExtension("/Users/li.li/Project_Papers/Mining4U/res/android-apis/android-1.xml"));
	}
	
	public static String getFileName(String filePath)
	{
		if (! filePath.contains("/"))
		{
			return filePath;
		}
		
		return filePath.substring(1 + filePath.lastIndexOf('/'));
	}
	
	public static String getFileNameWithoutExtension(String filePath)
	{
		String fileName = getFileName(filePath);
		
		String fileNameWithoutExtension = fileName.replaceAll("\\..*", "");
		
		return fileNameWithoutExtension;
	}
}
