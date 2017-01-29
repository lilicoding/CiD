package lu.uni.snt.mining4u.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.AndroidAPILifeModel;
import lu.uni.snt.mining4u.utils.CommonUtils;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.jimple.Stmt;

public class APITransformer extends BodyTransformer
{
	public Set<String> accessedAndroidAPIs = new HashSet<String>();
	public Map<String, Set<String>> api2callers = new HashMap<String, Set<String>>();

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		String methodSignature = b.getMethod().getSignature();
		
		if (methodSignature.startsWith("<android.support."))
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

				if (AndroidAPILifeModel.getInstance().isAndroidAPI(methodSig))
				{
					accessedAndroidAPIs.add(methodSig);
					CommonUtils.put(api2callers, methodSig, methodSignature);
				}
			}
		}
	}
}
