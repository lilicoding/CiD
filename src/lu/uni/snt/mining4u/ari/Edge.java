package lu.uni.snt.mining4u.ari;

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
