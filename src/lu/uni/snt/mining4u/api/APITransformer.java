package lu.uni.snt.mining4u.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.AndroidAPIs;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.jimple.Stmt;

public class APITransformer extends BodyTransformer
{
	public Set<String> accessedAndroidAPIs = new HashSet<String>();

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		System.out.println(b);
		
		String methodSignature = b.getMethod().getSignature();
		
		if (methodSignature.startsWith("<android.support.v"))
		{
			return;
		}
		
		PatchingChain<Unit> units = b.getUnits();
		
		for (Iterator<Unit> unitIter = units.snapshotIterator(); unitIter.hasNext(); )
		{
			Stmt stmt = (Stmt) unitIter.next();
			
			if (stmt.containsInvokeExpr())
			{
				String methodSig = stmt.getInvokeExpr().getMethod().getSignature();

				if (AndroidAPIs.getInstance().isAndroidAPI(methodSig))
				{
					accessedAndroidAPIs.add(methodSig);
				}
			}
		}
	}
}
