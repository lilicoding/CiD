package lu.uni.snt.mining4u.toolkits;

import lu.uni.snt.mining4u.AndroidAPILifeModel;

public class AndroidAPILifeModelBuilder 
{
	public static void main(String[] args) 
	{
		build();
		
		/*
		try
		{
			FileInputStream fis = new FileInputStream("release/res/apilife.model");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			AndroidAPILifeModel instance = (AndroidAPILifeModel) ois.readObject();
			ois.close();
			fis.close();
			
			System.out.println(instance.method2APILifes.keySet().size());
			if (instance.method2APILifes.keySet().contains("<android.util.FloatMath: float sqrt(float)>"))
			{
				System.out.println("Contain <android.util.FloatMath: float sqrt(float)>");
			}
			
			
			fis = new FileInputStream("res/apilife.model");
			ois = new ObjectInputStream(fis);
			AndroidAPILifeModel instance2 = (AndroidAPILifeModel) ois.readObject();
			ois.close();
			fis.close();
			
			System.out.println(instance2.method2APILifes.keySet().size());
			if (instance2.method2APILifes.keySet().contains("<android.util.FloatMath: float sqrt(float)>"))
			{
				System.out.println("Contain <android.util.FloatMath: float sqrt(float)>");
			}
			
			if (instance2.method2APILifes.containsKey("<android.renderscript.ProgramFragmentFixedFunction.Builder: android.renderscript.ProgramFragmentFixedFunction.Builder setPointSpriteTexCoordinateReplacement(boolean)>"))
			{
				System.out.println("Contain <android.renderscript.ProgramFragmentFixedFunction.Builder: android.renderscript.ProgramFragmentFixedFunction.Builder setPointSpriteTexCoordinateReplacement(boolean)>");
			}
			
			Set<String> methods = instance.method2APILifes.keySet();
			methods.removeAll(instance2.method2APILifes.keySet());
			
			
			for (String method : methods)
			{
				System.out.println(method);
			}
		}
		catch (IOException | ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		*/
	}
	
	public static void build()
	{
		AndroidAPILifeModel.getInstance().serialize();
	}
}
