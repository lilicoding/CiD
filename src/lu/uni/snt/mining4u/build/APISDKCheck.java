package lu.uni.snt.mining4u.build;

import java.util.HashSet;
import java.util.Set;

import lu.uni.snt.mining4u.api.APILife;

public class APISDKCheck 
{
	String signature = "";
	
	public Set<String> constraints = new HashSet<String>();
	
	// For APILife extension
	public boolean noUpperBound = false;
	public boolean noLowerBound = false;
	
	// For verifying if SDK check is properly implemented
	public int upperBoundValue = -1;
	public int lowerBoundValue = -1;
	
	public APISDKCheck(String signature, Set<String> constraints)
	{
		this.signature = signature;
		this.constraints = constraints;
		
		for (String constraint : constraints)
		{
			String condition = constraint.substring(constraint.indexOf(':') + 1);
			
			if (constraint.startsWith("MATCH:"))
			{
				resolveConstraint(condition, true);
			}
			else // NO-MATCH:
			{
				resolveConstraint(condition, false);
			}
		}
	}
	
	public APILife toAPILife()
	{
		return new APILife(signature, lowerBoundValue, upperBoundValue);
	}
	
	/**
	 * Here, we are only interested in >, <, >=, <=
	 * 
	 * @param condition
	 */
	public void resolveConstraint(String condition, boolean match)
	{
		if (condition.contains("<="))
		{
			String[] strs = condition.split("<=");
			
			String sdkVersionStr = strs[0].contains("$i") ? strs[1] : strs[0];
			int sdkVersion = Integer.parseInt(sdkVersionStr.trim());
			
			if (match)
			{
				//$i <= 10
				//=> upperBoundValue = 10
				
				noUpperBound = true;
				upperBoundValue = sdkVersion;
			}
			else
			{
				//$i <= 10 --> $i > 10 --> $i >= 11
				//=> lowerBoundValue = 11
				
				noLowerBound = true;
				lowerBoundValue = sdkVersion + 1;
			}
		}
		else if (condition.contains(">="))
		{
			String[] strs = condition.split(">=");
			
			String sdkVersionStr = strs[0].contains("$i") ? strs[1] : strs[0];
			int sdkVersion = Integer.parseInt(sdkVersionStr.trim());
			
			if (match)
			{
				noLowerBound = true;
				lowerBoundValue = sdkVersion;
			}
			else
			{
				noUpperBound = true;
				upperBoundValue = sdkVersion - 1;
			}
		}
		else if (condition.contains("<"))
		{
			String[] strs = condition.split("<");
			
			String sdkVersionStr = strs[0].contains("$i") ? strs[1] : strs[0];
			int sdkVersion = Integer.parseInt(sdkVersionStr.trim());
			
			if (match)
			{
				noUpperBound = true;
				upperBoundValue = sdkVersion - 1;
			}
			else
			{
				noLowerBound = true;
				lowerBoundValue = sdkVersion;
			}
		}
		else if (condition.contains(">"))
		{
			String[] strs = condition.split(">");
			
			String sdkVersionStr = strs[0].contains("$i") ? strs[1] : strs[0];
			int sdkVersion = Integer.parseInt(sdkVersionStr.trim());
			
			if (match)
			{
				noLowerBound = true;
				lowerBoundValue = sdkVersion + 1;
			}
			else
			{
				noUpperBound = true;
				upperBoundValue = sdkVersion;
			}
		}
	}
}
