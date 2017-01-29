package lu.uni.snt.mining4u.utils;

public class MethodSignature 
{
	private String signature = "";
	
	private String pkg;
	private String cls;
	private String methodName;
	private String returnType;
	
	private int parameterNumber;
	private String[] parameterTypes;
	
	private boolean containsGenericType = false;
	
	public MethodSignature(String signature)
	{
		this.signature = signature;
		parse(signature);
	}
	
	public boolean validateSignature(String signature)
	{
		String regex = "<\\w: *\\w *<*\\w>*(.*)>";
		return signature.matches(regex);
	}
	
	public void parse(String signature)
	{
		int posColon = signature.indexOf(':');
		cls = signature.substring(1, posColon);

		
		ClassSignature cs = new ClassSignature(cls);
		pkg = cs.getPackageName();
		
		int posStartBracket = signature.indexOf('(');
		int posSpaceBeforeMethodName = signature.lastIndexOf(' ', posStartBracket);
		
		returnType = signature.substring(posColon+2, posSpaceBeforeMethodName);
		methodName = signature.substring(posSpaceBeforeMethodName+1, posStartBracket);

		int posEndBracket = signature.lastIndexOf(')');
		String parameters = signature.substring(posStartBracket+1, posEndBracket);
		
		if (parameters.isEmpty())
		{
			parameterNumber = 0;
			parameterTypes = null;
		}
		else
		{
			if (parameters.contains(","))
			{
				String params[] = parameters.split(",");
				parameterNumber = params.length;
				parameterTypes = new String[parameterNumber];
				
				for (int i = 0; i < parameterNumber; i++)
				{
					parameterTypes[i] = params[i].trim();
				}
			}
			else
			{
				parameterNumber = 1;
				parameterTypes = new String[parameterNumber];
				parameterTypes[0] = parameters.trim();
			}
		}
	}

	/**
	 * 
	 * Remove Generic programming introduced items, such as <java.lang.String>, <T>, <?>, <? extends Object>, ... 
	 * 
	 * @param methodSig
	 * @return
	 */
	public String getSignatureWithoutGPItems()
	{
		returnType = returnType.replaceAll("<.+>", "");
		if (returnType.length() == 1)
		{
			containsGenericType = true;
		}
		
		String paramStr = "";
		if (null != parameterTypes)
		{
			for (int i = 0; i < parameterTypes.length; i++)
			{
				if (parameterTypes[i].length() == 1)
				{
					containsGenericType = true;
				}
				
				if (0 == i)
					paramStr = paramStr + parameterTypes[i];
				else
					paramStr = paramStr + "," + parameterTypes[i];
			}
		}
		
		paramStr = paramStr.replaceAll("<.+>", "");
		
		String sig = "<" + cls + ": " + returnType + " " + methodName + "(" + paramStr + ")>";
		
		if (containsGenericType)
		{
			System.out.println(sig);
		}
		
		return sig;
	}
	
	
	
	public String getCompactSignature()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(cls);
		sb.append("." + methodName);
		
		/*if (null != parameterTypes)
		{
			sb.append(parameterTypes.length);
		}*/
		
		return sb.toString();
	}
	
	public String getCompactSignatureWithParams()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(cls);
		sb.append("." + methodName);
		
		for (String param : parameterTypes)
		{
			sb.append("." + param);
		}
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}
	
	public boolean containsVarargs()
	{
		return this.signature.contains("...");
	}
	
	public boolean containsGenericType() 
	{
		boolean containsGenericType = false;
		
		returnType = returnType.replaceAll("<.+>", "").replaceAll("\\.\\.\\.", "");
		if (returnType.length() == 1)
		{
			containsGenericType = true;
		}
		
		if (null != parameterTypes)
		{
			for (int i = 0; i < parameterTypes.length; i++)
			{
				parameterTypes[i] = parameterTypes[i].replaceAll("<.+>", "").replaceAll("\\.\\.\\.", "");
				
				if (parameterTypes[i].length() == 1)
				{
					containsGenericType = true;
				}
			}
		}
		
		return containsGenericType;
	}

	public String getSignature() {
		return signature;
	}

	public String getPkg() {
		return pkg;
	}

	public String getCls() {
		return cls;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public int getParameterNumber() {
		return parameterNumber;
	}

	public String[] getParameterTypes() {
		return parameterTypes;
	}
}
