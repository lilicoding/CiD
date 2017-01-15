package lu.uni.snt.mining4u.build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.AndroidAPILifeModel;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class AndroidSDKVersionChecker2 extends BodyTransformer 
{
	public static void main(String[] args) 
	{
		AndroidSDKVersionChecker2.scan("testapps/000040123DF387DFBBB46132E26CE74A75D3935E5A04656E48EBB882107E953F.apk", "/Users/li.li/Project/github/android-platforms");
	}

	private static Map<String, APISDKCheck> method2SDKChecks = new HashMap<String, APISDKCheck>();
	
	private StringBuilder buildContent = null;
	public AndroidSDKVersionChecker2()
	{
		buildContent = new StringBuilder();
	}
	
	public static Map<String, APISDKCheck> scan(String apkPath, String androidJars)
	{
		AndroidSDKVersionChecker2 builder = new AndroidSDKVersionChecker2();
		builder.run(apkPath, androidJars);
		
		return method2SDKChecks;
	}
	
	public void run(String apkPath, String androidJars)
	{
		G.reset();
		
		String[] args =
        {
			"-process-dir", apkPath,
            "-ire",
			"-pp",
			"-keep-line-number",
			"-allow-phantom-refs",
			"-w",
			"-p", "cg", "enabled:false",
			"-src-prec", "apk"
        };
			
		Options.v().set_output_format(Options.output_format_none);
		Options.v().set_android_jars(androidJars);
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.AndroidOSBuilder", this));
		soot.Main.main(args);
		
		G.reset();
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		String clsName = b.getMethod().getDeclaringClass().getName();
		
		if (! clsName.startsWith("android.support") && b.toString().contains("<android.os.Build$VERSION: int SDK_INT>"))
		{
			buildContent.append(b.getMethod().getSignature());
			buildContent.append(b);
			
			ExceptionalUnitGraph graph = new ExceptionalUnitGraph(b);
			PatchingChain<Unit> units = b.getUnits();
			
			for (Iterator<Unit> unitIter = units.snapshotIterator(); unitIter.hasNext(); )
			{
				Stmt stmt = (Stmt) unitIter.next();
				
				if (stmt.containsInvokeExpr())
				{
					String methodSig = stmt.getInvokeExpr().getMethod().getSignature();
					if (AndroidAPILifeModel.getInstance().isAndroidAPI(methodSig))
					{
						Set<String> constraints = new HashSet<String>();
						collectConstraint(stmt, graph, constraints, new HashSet<Unit>());
						
						if (! constraints.isEmpty())
						{
							if (method2SDKChecks.containsKey(methodSig))
							{
								Set<String> tmpConstraints = new HashSet<String>();
								for (String constraint : constraints)
								{
									tmpConstraints.add(constraint);
								}
								
								tmpConstraints.removeAll(method2SDKChecks.get(methodSig).constraints);
								
								if (! tmpConstraints.isEmpty())
								{
									System.out.println("CONFLICT (Existing):" + methodSig + "," + method2SDKChecks.get(methodSig).constraints);
									System.out.println("CONFLICT (New):" + methodSig + "," + constraints);
								}
								
								APISDKCheck sdkCheck = method2SDKChecks.get(methodSig);
								sdkCheck.constraints.addAll(constraints);
								method2SDKChecks.put(methodSig, sdkCheck);
							}
							else
							{
								APISDKCheck sdkCheck = new APISDKCheck(methodSig, constraints);
								method2SDKChecks.put(methodSig, sdkCheck);
							}
						}
						
						
					}
				}
			}
		}
	}
	
	public void collectConstraint(Stmt stmt, ExceptionalUnitGraph graph, Set<String> conditions, Set<Unit> visitedStmts)
	{
		visitedStmts.add(stmt);
		
		List<Unit> units = graph.getPredsOf(stmt);
		for (Unit unit : units)
		{
			if (visitedStmts.contains(unit))
			{
				continue;
			}
			
			if (unit instanceof IfStmt)
			{
				List<Unit> preUnits = graph.getPredsOf(unit);
				for (Unit preUnit : preUnits)
				{
					if (preUnit instanceof AssignStmt && ((Stmt) preUnit).containsFieldRef())
					{
						AssignStmt assignStmt = (AssignStmt) preUnit;
						
						if (assignStmt.getRightOp().toString().equals("<android.os.Build$VERSION: int SDK_INT>"))
						{
							IfStmt ifStmt = (IfStmt) unit;

							if (ifStmt.getTarget().equals(stmt))
							{
								conditions.add("MATCH:" + ifStmt.getCondition());
							}
							else
							{
								conditions.add("NO-MATCH:" + ifStmt.getCondition());
							}
						}
					}
				}
			}
			
			collectConstraint((Stmt) unit, graph, conditions, visitedStmts);
		}
	}

}
