package lu.uni.snt.mining4u;
import soot.SootMethod;
import soot.Type;


public class MethodSig 
{
	public static void main(String[] args)
	{
		System.out.println(simplify("Builder"));
	}
	
	public static String getMethodSig(SootMethod sootMethod)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<");
		sb.append(sootMethod.getDeclaringClass().getName());
		sb.append(": ");
		
		String returnType = sootMethod.getReturnType().getEscapedName();
		returnType = simplify(returnType);
		
		sb.append(returnType);
		sb.append(" " + sootMethod.getName());
		
		sb.append("(");
		
		boolean first = true;
		
		for (Type type : sootMethod.getParameterTypes())
		{
			if (first)
			{
				sb.append(simplify(type.getEscapedName()));
				first = false;
			}
			else
			{
				sb.append("," + simplify(type.getEscapedName()));
			}
		}
		
		sb.append(")>");
		
		return sb.toString();
	}
	
	public static String simplify(String type)
	{
		String[] strs = type.split("\\.");
		return strs[strs.length-1];
	}
}
