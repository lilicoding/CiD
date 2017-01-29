package lu.uni.snt.mining4u.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CommonUtils 
{
	public static boolean isStringEmpty(String str)
	{
		boolean isEmpty = false;
		
		if (null == str || str.isEmpty())
		{
			isEmpty = true;
		}
		
		return isEmpty;
	}
	
	
	public static String getFileName(String path)
	{
		String fileName = path;
		if (fileName.contains("/"))
		{
			fileName = fileName.substring(fileName.lastIndexOf('/')+1);
		}
		
		return fileName;
	}
	
	public static void writeResultToFile(String path, String content)
	{
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
		    out.print(content);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static int totalValue(Map<String, Integer> map)
	{
		int total = 0;
		
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			total += entry.getValue();
		}
		
		return total;
	}
	
	public static Set<String> loadFile(String filePath)
	{
		Set<String> lines = new HashSet<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				lines.add(line);
			}
			
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return lines;
	}
	
	public static List<String> loadFileToList(String filePath)
	{
		List<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				lines.add(line);
			}
			
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return lines;
	}
	
	public static List<String> loadFileToList(String filePath, String prefix)
	{
		if ("NULL".equals(prefix))
		{
			return loadFileToList(filePath);
		}
		
		List<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				if (null != prefix && line.startsWith(prefix))
				{
					lines.add(line.replace(prefix, ""));
				}
			}
			
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return lines;
	}
	
	public static TreeMap<String,Integer> sort(Map<String, Integer> map)
	{
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		sorted_map.putAll(map);
		
		return sorted_map;
	}
	
	static class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
	public static double computeSimilarity(int identical, int similar, int _new, int deleted)
	{
		int total1 = identical + similar + deleted;
		int total2 = identical + similar + _new; 
		
		int total = total1 < total2 ? total1 : total2;
		
		double rate = (double) identical / (double) total;
		
		return rate;
	}
	
	public static void put(Map<String, Set<String>> map1, String key, String value)
	{
		if (map1.containsKey(key))
		{
			Set<String> values = map1.get(key);
			if (values == null)
			{
				System.out.println("XX");
			}
			values.add(value);
			map1.put(key, values);
		}
		else
		{
			Set<String> values = new HashSet<String>();
			values.add(value);
			map1.put(key, values);
		}
	}
	
	public static void put(Map<String, Set<String>> dest, Map<String, Set<String>> src)
	{
		for (Map.Entry<String, Set<String>> entry : src.entrySet())
		{
			String cls = entry.getKey();
			Set<String> set2 = entry.getValue();
			
			if (dest.containsKey(cls))
			{
				Set<String> set1 = dest.get(cls);
				//Different API level may introduce different classes
				if (set1 == null)
				{
					set1 = new HashSet<String>();
				}
				set1.addAll(set2);
				
				dest.put(cls, set1);
			}
			else
			{
				Set<String> set1 = new HashSet<String>();
				set1.addAll(set2);
				dest.put(cls, set1);
			}
		}
	}
}
