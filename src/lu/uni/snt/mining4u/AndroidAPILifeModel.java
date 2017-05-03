package lu.uni.snt.mining4u;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.api.APILife;
import lu.uni.snt.mining4u.utils.CommonUtils;
import lu.uni.snt.mining4u.utils.MethodSignature;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class AndroidAPILifeModel implements Serializable
{	
	private static final long serialVersionUID = 1785987027002129118L;
	
	public Map<String, Set<String>> class2SuperClasses = new HashMap<String, Set<String>>();
	public Map<String, Set<String>> class2Methods = new HashMap<String, Set<String>>();
	public Map<String, APILife> method2APILifes = new HashMap<String, APILife>();
	
	//For such APIs that contain generic types or contain varargs.
	public Map<String, Set<String>> compactSig2Methods = new HashMap<String, Set<String>>();
	
	//compactSig2Methods = compactSig2Methods_gt U compactSig2Methods_varargs
	public Map<String, Set<String>> compactSig2Methods_gt = new HashMap<String, Set<String>>();
	public Map<String, Set<String>> compactSig2Methods_varargs = new HashMap<String, Set<String>>();
	
	public Map<String, String> method2inheritedAPIs = new HashMap<String, String>();
	
	private static AndroidAPILifeModel instance = null;
	private static String modelPath = "res/android_api_model.txt";
	
	private String lifetimeAPIPath = "res/android_api_lifetime.txt";
	private String genericAPIPath = "res/android_api_generictype.txt";
	private String varargsAPIPath = "res/android_api_varargs.txt";
	private String androidAPIsDirPath = "res/android-apis"; 
	
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
			
			Set<String> genericAPIs = CommonUtils.loadFile(instance.genericAPIPath);
			for (String genericAPI : genericAPIs)
			{
				String compactSig = new MethodSignature(genericAPI).getCompactSignature();
				CommonUtils.put(instance.compactSig2Methods, compactSig, genericAPI);
				CommonUtils.put(instance.compactSig2Methods_gt, compactSig, genericAPI);
			}
			
			Set<String> varargsAPIs = CommonUtils.loadFile(instance.varargsAPIPath);
			for (String varargsAPI : varargsAPIs)
			{
				String compactSig = new MethodSignature(varargsAPI).getCompactSignature();
				CommonUtils.put(instance.compactSig2Methods, compactSig, varargsAPI);
				CommonUtils.put(instance.compactSig2Methods_varargs, compactSig, varargsAPI);
			}
			
			//CommonUtils.put(instance.compactSig2Methods, instance.compactSig2Methods_gt);
			//CommonUtils.put(instance.compactSig2Methods, instance.compactSig2Methods_varargs);
		}

		return instance;
	}
	
	private AndroidAPILifeModel()
	{
		File androidAPIsDir = new File(androidAPIsDirPath);
		for (File file : androidAPIsDir.listFiles())
		{
			FrameworkBase fb = new FrameworkBase();
			
			fb.load(file.getAbsolutePath());
			
			CommonUtils.put(class2SuperClasses, fb.class2SuperClasses);
			CommonUtils.put(class2Methods, fb.class2Methods);
		}
		
		Set<String> lines = CommonUtils.loadFile(lifetimeAPIPath);
		for (String line : lines)
		{
			APILife apiLife = new APILife(line);
			method2APILifes.put(apiLife.getSignature(), apiLife);
		}
	}
	
	public boolean containsGenericType(String methodSig)
	{
		methodSig = methodSig.replace("$", ".");
		
		MethodSignature ms = new MethodSignature(methodSig);
		String compactSig = ms.getCompactSignature();
		
		if (compactSig2Methods_gt.containsKey(compactSig))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean containsVarargs(String methodSig)
	{
		methodSig = methodSig.replace("$", ".");
		
		MethodSignature ms = new MethodSignature(methodSig);
		String compactSig = ms.getCompactSignature();
		
		if (compactSig2Methods_varargs.containsKey(compactSig))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Extension
	 * Generic Type
	 * Varargs
	 * 
	 * @param methodSig
	 * @return
	 */
	public boolean isAndroidAPI(String methodSig)
	{	
		methodSig = methodSig.replace("$", ".");
		
		if (method2APILifes.containsKey(methodSig))
		{
			return true;
		}
		else 
		{	
			String compatMethodSig = new MethodSignature(methodSig).getCompactSignature();
			
			if (compactSig2Methods_gt.containsKey(compatMethodSig))
			{
				if (Config.DEBUG)
					System.out.println("[DEBUG] Generic Programming:" + methodSig + " is an Android API with generic type");
				
				return true;
			}
			else if (compactSig2Methods_varargs.containsKey(compatMethodSig))
			{
				if (Config.DEBUG)
					System.out.println("[DEBUG] Varargs:" + methodSig + " is an Android API with varargs");
				
				return true;
			}
		}
		

		return false;
	}
	
	public boolean isInheritedAndroidAPI(String methodSig)
	{
		try
		{
			SootMethod sootMethod = Scene.v().getMethod(methodSig);
			
			SootClass sootClass = sootMethod.getDeclaringClass();
			
			List<SootClass> workList = new LinkedList<SootClass>();
			if (sootClass.hasSuperclass())
			{
				workList.add(sootClass.getSuperclass());
			}
			for (Iterator<SootClass> iter = sootClass.getInterfaces().snapshotIterator(); iter.hasNext(); )
			{
				workList.add(iter.next());
			}
			
			while (! workList.isEmpty())
			{
				sootClass = workList.remove(0);
				
				String newMethodSig = methodSig.replace("<" + sootMethod.getDeclaringClass().getName() + ":", "<" + sootClass.getName() + ":");
				
				if (isAndroidAPI(newMethodSig))
				{
					method2inheritedAPIs.put(methodSig, newMethodSig);
					return true;
				}
				else
				{
					if (sootClass.hasSuperclass())
					{
						workList.add(sootClass.getSuperclass());
					}
					for (Iterator<SootClass> iter = sootClass.getInterfaces().snapshotIterator(); iter.hasNext(); )
					{
						workList.add(iter.next());
					}
				}
			}
		}
		catch (Exception ex)
		{
			//TODO:
		}
		
		return false;
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
	        
			System.out.printf("The API Life Model is Serialized into file res/android_api_model.txt");
	    }
		catch(IOException ex) 
		{
			ex.printStackTrace();
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
		methodSignature = methodSignature.replace("$", ".");
		
		APILife apiLife = new APILife(methodSignature, -1, -1);
		
		MethodSignature sig = new MethodSignature(methodSignature);
		
		if (! method2APILifes.containsKey(methodSignature))
		{
			Set<String> methods = compactSig2Methods.get(sig.getCompactSignature());
			if (null != methods)
			{
				for (String methodSig : methods)
				{
					MethodSignature ms = new MethodSignature(methodSig);
					if (ms.containsGenericType())
					{
						if (Config.DEBUG)
							System.out.println("[DEBUG]: GT Found, " + methodSig + "-->" + methodSignature);
						//refine(apiLife, methodSig);
					}
					else
					{
						if (Config.DEBUG)
							System.out.println("[DEBUG]: Varargs Found, " + methodSig + "-->" + methodSignature);
					}
					
					if (ms.containsGenericReturnType() ||
						ms.getReturnType().equals(sig.getReturnType()))
					{
						refine(apiLife, methodSig);
					}
					
					// To be more precise: ms.containsGenericType() && ms.containsVarargs()
				}
			}
		}
		else
		{
			refine(apiLife, methodSignature);
		}
		
		return apiLife;
	}
	
	public APILife refine(APILife current, String methodSignature)
	{
		if (method2APILifes.containsKey(methodSignature))
		{
			APILife target = method2APILifes.get(methodSignature);
			
			if (current.getMinAPILevel() == -1 || current.getMinAPILevel() > target.getMinAPILevel())
			{
				current.setMinAPILevel(target.getMinAPILevel());
			}
			if (current.getMinAPILevel() == -1 || current.getMaxAPILevel() < target.getMaxAPILevel())
			{
				current.setMaxAPILevel(target.getMaxAPILevel());
			}
		}
		
		
		MethodSignature sig = new MethodSignature(methodSignature);
		String cls = sig.getCls();
		if (class2SuperClasses.containsKey(cls))
		{
			for (String superCls : class2SuperClasses.get(cls))
			{
				current = refine(current, cls, superCls, methodSignature);
			}
		}
		
		return current;
	}
	
	
	public APILife refine(APILife current, String currentCls, String superCls, String methodSignature)
	{
		String newMethodSig = methodSignature.replace(currentCls + ":", superCls + ":");
		
		if (method2APILifes.containsKey(newMethodSig))
		{
			if (Config.DEBUG)
				System.out.println("[DEBUG] SuperClass:" + current.getSignature() + "-->" + newMethodSig);
			
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
				current = refine(current, currentCls, superSuperCls, newMethodSig);
			}
		}
		
		return current;
	}
}
