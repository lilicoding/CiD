package lu.uni.snt.mining4u;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.api.APILife;
import lu.uni.snt.mining4u.build.APISDKCheck;
import lu.uni.snt.mining4u.methodextractor.FrameworkBase;
import lu.uni.snt.mining4u.utils.CommonUtils;
import lu.uni.snt.mining4u.utils.MethodSignature;

public class AndroidAPILifeModel implements Serializable
{	
	private static final long serialVersionUID = 1785987027002129118L;
	
	public Map<String, Set<String>> class2SuperClasses = new HashMap<String, Set<String>>();
	public Map<String, Set<String>> class2Methods = new HashMap<String, Set<String>>();
	public Map<String, APILife> method2APILifes = new HashMap<String, APILife>();
	
	private static AndroidAPILifeModel instance = null;
	private static final String modelPath = "res/apilife.model";
	
	public static AndroidAPILifeModel getInstance()
	{
		if (null == instance)
		{
			File model = new File(modelPath);
			if (model.exists())
			{
				try
				{
					FileInputStream fis = new FileInputStream(modelPath);
					ObjectInputStream ois = new ObjectInputStream(fis);
					
					instance = (AndroidAPILifeModel) ois.readObject();
					ois.close();
					fis.close();
				}
				catch (IOException | ClassNotFoundException ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				instance = new AndroidAPILifeModel();
				instance.serialize();
			}
		}

		return instance;
	}
	
	private AndroidAPILifeModel()
	{
		File androidAPIsDir = new File("res/android-apis");
		for (File file : androidAPIsDir.listFiles())
		{
			FrameworkBase fb = new FrameworkBase();
			
			fb.load(file.getAbsolutePath());
			
			put(class2SuperClasses, fb.class2SuperClasses);
			put(class2Methods, fb.class2Methods);
		}
		
		Set<String> lines = CommonUtils.loadFile("res/android_api_life.txt");
		for (String line : lines)
		{
			APILife apiLife = new APILife(line);
			method2APILifes.put(apiLife.getSignature(), apiLife);
		}
	}
	
	public boolean isAndroidAPI(String methodSig)
	{
		if (method2APILifes.containsKey(methodSig))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void serialize()
	{
		try 
		{
			FileOutputStream fos = new FileOutputStream(modelPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
	        
			System.out.printf("The API Life Model is Serialized into file res/apilife.model");
	    }
		catch(IOException ex) 
		{
			ex.printStackTrace();
		}
	}
	
	private void put(Map<String, Set<String>> map1, Map<String, Set<String>> map2)
	{
		for (Map.Entry<String, Set<String>> entry : map2.entrySet())
		{
			String cls = entry.getKey();
			Set<String> set2 = entry.getValue();
			
			if (map1.containsKey(cls))
			{
				Set<String> set1 = map1.get(map1);
				//Different API level may introduce different classes
				if (set1 == null)
				{
					set1 = new HashSet<String>();
				}
				set1.addAll(set2);
				
				map1.put(cls, set1);
			}
			else
			{
				Set<String> set1 = new HashSet<String>();
				set1.addAll(set2);
				map1.put(cls, set1);
			}
		}
	}
	
	/**
	 * To also check the lifetime of its super classes
	 * 
	 * @param methodSignature
	 * @return
	 */
	public APILife getLifetime(String methodSignature)
	{
		APILife apiLife = new APILife(methodSignature, -1, -1);
		
		if (method2APILifes.containsKey(methodSignature))
		{
			APILife target = method2APILifes.get(methodSignature);
			
			apiLife.setMinAPILevel(target.getMinAPILevel());
			apiLife.setMaxAPILevel(target.getMaxAPILevel());
		}

		MethodSignature sig = new MethodSignature(methodSignature);
		String cls = sig.getCls();

		if (class2SuperClasses.containsKey(cls))
		{
			for (String superCls : class2SuperClasses.get(cls))
			{
				apiLife = refine(apiLife, cls, superCls);
			}
		}
		
		return apiLife;
	}
	
	public APILife getLifetime(String methodSignature, Map<String, APISDKCheck> method2SDKChecks)
	{
		APILife apiLife = getLifetime(methodSignature);
		
		if (method2SDKChecks.containsKey(methodSignature))
		{
			APISDKCheck sdkCheck = method2SDKChecks.get(methodSignature);
			
			if (sdkCheck.noUpperBound)
			{
				System.out.println("EXTEND (UPPER_BOUND):" + apiLife.getMaxAPILevel() + ", (checked)" + sdkCheck.upperBoundValue);
				apiLife.setMaxAPILevel(Config.LATEST_SDK_VERSION);
			}
			
			if (sdkCheck.noLowerBound)
			{
				System.out.println("EXTEND (LOWER_BOUND):" + apiLife.getMinAPILevel() + ", (checked)" + sdkCheck.lowerBoundValue);
				apiLife.setMinAPILevel(1);
			}
		}
		
		return apiLife;
	}
	
	public APILife refine(APILife current, String currentCls, String superCls)
	{
		String newMethodSig = current.getSignature().replace(currentCls + ":", superCls + ":");
		
		if (method2APILifes.containsKey(newMethodSig))
		{
			APILife target = method2APILifes.get(newMethodSig);
			
			if (current.getMinAPILevel() == -1 || current.getMinAPILevel() > target.getMinAPILevel())
			{
				current.setMinAPILevel(target.getMinAPILevel());
			}
			if (current.getMinAPILevel() == -1 || current.getMaxAPILevel() < target.getMaxAPILevel())
			{
				current.setMaxAPILevel(target.getMaxAPILevel());
			}
		}
	
		if (class2SuperClasses.containsKey(superCls))
		{
			for (String superSuperCls : class2SuperClasses.get(superCls))
			{
				current = refine(current, currentCls, superSuperCls);
			}
		}
		
		return current;
	}
}
