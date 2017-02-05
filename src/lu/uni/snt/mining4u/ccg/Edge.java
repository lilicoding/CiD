package lu.uni.snt.mining4u.ccg;

import java.util.HashSet;
import java.util.Set;

public class Edge 
{
	public String srcSig = "";
	public String tgtSig = "";
	
	public Set<String> conditions = new HashSet<String>();
	
	@Override
	public String toString() 
	{
		return conditions + ":" + srcSig + "-->" + tgtSig;
	}
}
