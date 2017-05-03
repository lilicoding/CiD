package lu.uni.snt.cid.dcl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class DexHunter 
{	
	public static void main(String[] args) throws ZipException, IOException
	{
		String apkPath = "/Volumes/joey/workspace/piggyback/groundtruth/apps/82ECB0417DF1E874BEF43D0EA5A424A70E95C6689DD1C0229F43164B3719BA75.apk";

		DexHunter hunter = new DexHunter(apkPath);
		Set<String> dexes = hunter.hunt();
		for (String dex : dexes)
			System.out.println(dex);
	}
	
	private String apkPath;
	private String apkName;
	private Set<String> dexes;
	
	public DexHunter(String apkPath)
	{
		this.apkPath = apkPath;
		apkName = apkPath.substring(1 + apkPath.lastIndexOf('/'));
		
		dexes = new HashSet<String>();
	}
	
	public Set<String> hunt()
	{
		try 
		{
			extractAdditionalDexes(apkPath, apkName + ".unzip");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return dexes;
	}
	
	public void extractAdditionalDexes(String zipFile, String targetDir) throws ZipException, IOException
	{
	    int BUFFER = 2048;
	    ZipFile zip = new ZipFile(new File(zipFile));
	    
	    new File(targetDir).mkdir();
	    
	    Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

	    while (zipFileEntries.hasMoreElements())
	    {
	        ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	        
	        String currentEntry = entry.getName();
	        File destFile = new File(targetDir, currentEntry);
	        File destinationParent = destFile.getParentFile();

	        destinationParent.mkdirs();

	        if (! entry.isDirectory())
	        {
	            BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
	            int currentByte;
	            byte data[] = new byte[BUFFER];
	            FileOutputStream fos = new FileOutputStream(destFile);
	            BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

	            while ((currentByte = is.read(data, 0, BUFFER)) != -1) 
	            {
	                dest.write(data, 0, currentByte);
	            }
	            dest.flush();
	            dest.close();
	            is.close();
	        }
	        
	        if (destFile.isFile() && destFile.getName().endsWith(".dex"))
	        {
	        	if (! destFile.getAbsolutePath().endsWith(apkName + ".unzip" + File.separator + "classes.dex"))
	        	{
	        		dexes.add(destFile.getAbsolutePath());
	        	}
	        }
	        
            if (isZipFile(destFile))
	      	{
            	extractAdditionalDexes(destFile.getAbsolutePath(), destFile.getAbsolutePath() + ".unzip");
	      	}
	    }
	    
	    zip.close();
	}
	
	public boolean isZipFile(File file) throws IOException 
	{
		if(file.isDirectory() || !file.canRead() || file.length() < 4) 
		{
			return false;
		}
		  
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		int test = in.readInt();
		in.close();
		
		return test == 0x504b0304;
	}
}
