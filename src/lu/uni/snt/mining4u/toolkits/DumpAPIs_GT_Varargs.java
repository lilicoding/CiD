package lu.uni.snt.mining4u.toolkits;

import java.util.Set;

import lu.uni.snt.mining4u.utils.CommonUtils;
import lu.uni.snt.mining4u.utils.MethodSignature;

public class DumpAPIs_GT_Varargs {

	public static void main(String[] args) 
	{
		Set<String> lines = CommonUtils.loadFile("res/android_api_lifetime.txt");

		StringBuilder gt = new StringBuilder();
		StringBuilder varargs = new StringBuilder();
		
		for (String line : lines)
		{
			int lastAngleBracketPos = line.lastIndexOf('>');
			String methodSig = line.substring(0, lastAngleBracketPos+1);
			
			MethodSignature ms = new MethodSignature(methodSig);
			if (ms.containsGenericType())
			{
				gt.append(methodSig + "\n");
			}
			if (ms.containsVarargs())
			{
				varargs.append(methodSig + "\n");
			}
		}
		
		CommonUtils.writeResultToFile("res/android_api_generictype.txt", gt.toString());
		CommonUtils.writeResultToFile("res/android_api_varargs.txt", varargs.toString());
	}

}
