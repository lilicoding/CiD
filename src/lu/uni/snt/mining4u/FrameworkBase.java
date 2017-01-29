package lu.uni.snt.mining4u;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.utils.CommonUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class FrameworkBase 
{
	public Map<String, Set<String>> class2SuperClasses = new HashMap<String, Set<String>>();
	public Map<String, Set<String>> class2Methods = new HashMap<String, Set<String>>();
	
	public void load(String androidAPIPath)
	{
		if (androidAPIPath.endsWith(".txt"))
		{
			parseTxtFile(androidAPIPath);
		}
		else if (androidAPIPath.endsWith(".xml"))
		{
			parseXmlFile(androidAPIPath);
		}
	}
	
	public void parseTxtFile(String txtFilePath)
	{
		List<String> lines = CommonUtils.loadFileToList(txtFilePath);
		
		String currentPkgName = "";
		String currentClsName = "";
		
		for (String line : lines)
		{
			line = line.trim();
			
			line = removeGenericType(line);
			
			if (line.startsWith("package"))
			{
				currentPkgName = line;
				currentPkgName = currentPkgName.replace("package", "").replace("{", "").trim();
			}
			else if (line.contains(" class "))
			{
				line = line.replaceAll(".*class ", "");
				line = currentPkgName + "." + line;
				line = line.replace("{", "").trim();
				
				Set<String> superClses = new HashSet<String>();
				
				if (line.contains("implements"))
				{
					String[] strs = line.split("implements");
					line = strs[0].trim();
					
					String[] interfaces = strs[1].trim().split(" ");
					for (String interf : interfaces)
					{
						superClses.add(interf);
					}
				}
				
				if (line.contains("extends"))
				{
					String[] strs = line.split("extends");
					line = strs[0].trim();
					
					superClses.add(strs[1].trim());
				}
				
				currentClsName = line;
				
				class2SuperClasses.put(currentClsName, superClses);
			}
			else if (line.startsWith("ctor") || line.startsWith("method"))
			{
				StringBuilder sb = new StringBuilder();
				
				if (line.startsWith("ctor"))
				{
					sb.append("<" + currentClsName + ": void <init>");
					
					String params = line.substring(line.lastIndexOf('('), line.lastIndexOf(')')+1).replace(" ", "");
					
					sb.append(params + ">");
				}
				else if (line.startsWith("method"))
				{
					sb.append("<" + currentClsName + ": ");
					
					String params = line.substring(line.lastIndexOf('('), line.lastIndexOf(')')+1).replace(" ", "");
					
					line = line.substring(0, line.lastIndexOf('('));
					String[] strs = line.split(" ");
					
					sb.append(strs[strs.length-2] + " " + strs[strs.length-1]);
					sb.append(params + ">");
				}
			
				put(class2Methods, currentClsName, sb.toString());
			}
		}
	}
	
	public void parseXmlFile(String xmlFilePath)
	{
		SAXBuilder builder = new SAXBuilder();
		
		try
		{
			Document doc = (Document) builder.build(xmlFilePath);
			Element api = doc.getRootElement();

			List<Element> packageEles = api.getChildren("package");
			for (Element packageEle : packageEles)
			{
				String packageName = packageEle.getAttributeValue("name");
				
				List<Element> classEles = packageEle.getChildren("class");
				for (Element classEle : classEles)
				{
					String className = packageName + "." + classEle.getAttributeValue("name");

					String extendedClass = classEle.getAttributeValue("extends");
					if (! "java.lang.Object".equals(extendedClass))
					{
						put(class2SuperClasses, className, classEle.getAttributeValue("extends"));
					}

					for (Element implementsEle : classEle.getChildren("implements"))
					{
						put(class2SuperClasses, className, implementsEle.getAttributeValue("name"));
					}
					
					for (Element constructorEle : classEle.getChildren("constructor"))
					{
						StringBuilder sb = new StringBuilder();
						sb.append("<" + className + ": void <init>(");
						
						boolean first = true;
						
						for (Element parameterEle : constructorEle.getChildren("parameter"))
						{
							if (first)
							{
								sb.append(removeGenericType(parameterEle.getAttributeValue("type")));
								first = false;
							}
							else
							{
								sb.append("," + removeGenericType(parameterEle.getAttributeValue("type")));
							}
						}
						
						sb.append(")>");
						
						put(class2Methods, className, sb.toString());
					}
					
					for (Element methodEle : classEle.getChildren("method"))
					{
						StringBuilder sb = new StringBuilder();
						sb.append("<" + className + ": ");
						sb.append(removeGenericType(methodEle.getAttributeValue("return")));
						sb.append(" " + methodEle.getAttributeValue("name"));
						sb.append("(");
						
						boolean first = true;
						
						for (Element parameterEle : methodEle.getChildren("parameter"))
						{
							if (first)
							{
								sb.append(removeGenericType(parameterEle.getAttributeValue("type")));
								first = false;
							}
							else
							{
								sb.append("," + removeGenericType(parameterEle.getAttributeValue("type")));
							}
						}
						
						sb.append(")>");
						
						put(class2Methods, className, sb.toString());
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static String removeGenericType(String str)
	{
		while (str.contains(">"))
		{
			int endAngleBracket = str.indexOf('>');
			int startAngleBracket = str.lastIndexOf('<', endAngleBracket);
			
			String strInAngleBracket = str.substring(startAngleBracket, endAngleBracket+1);
			
			str = str.replace(strInAngleBracket, "");
		}
		
		return str;
	}
	
	public void put(Map<String, Set<String>> class2Methods, String cls, String method)
	{
		if (class2Methods.containsKey(cls))
		{
			Set<String> methods = class2Methods.get(cls);
			methods.add(method);
			class2Methods.put(cls, methods);
		}
		else
		{
			Set<String> methods = new HashSet<String>();
			methods.add(method);
			class2Methods.put(cls, methods);
		}
	}
}
