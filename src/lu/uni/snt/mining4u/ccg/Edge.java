package lu.uni.snt.mining4u.ccg;

public class Edge 
{
	public String srcSig = "";
	public String tgtSig = "";
	
	public String conditions = "";

	@Override
	public String toString() 
	{
		return conditions + ":" + srcSig + "-->" + tgtSig;
	}
}
