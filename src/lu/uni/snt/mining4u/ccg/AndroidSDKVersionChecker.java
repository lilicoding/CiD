package lu.uni.snt.mining4u.ccg;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.utils.SootUtils;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class AndroidSDKVersionChecker extends BodyTransformer 
{
	public static void main(String[] args) 
	{
		/*
		new AndroidSDKVersionChecker().scan("/Volumes/joey/workspace/mudflow_benign_apps/0088969C4F4B03A537A257FD43B1C8552372724AD410B6A477AC48CB881AB51E.apk", 
				"/Users/li.li/Project/github/android-platforms");
		
		
		for (String methodSig : ConditionalCallGraph.srcMethod2edges.keySet())
		{
			for (Edge e : ConditionalCallGraph.srcMethod2edges.get(methodSig))
			{
				if (! e.conditions.contains("[]"))
					System.out.println(e.conditions + ":" +  e.srcSig + "-->" + e.tgtSig);
			}
		}
		*/
	}
	
	
	public static void scan(String apkPath, String androidJars)
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
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.AndroidSDKVersionChecker", new AndroidSDKVersionChecker()));
		soot.Main.main(args);
		
		G.reset();
		
		ConditionalCallGraph.expandConstructors();
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		//System.out.print(b);
		
		if (b.getMethod().getSignature().equals("<android.graphics.drawable.AnimatedVectorDrawable: void start()>"))
		{
			System.out.println("what the fuck" + b);
		}
		
		if (! b.getMethod().getDeclaringClass().getName().startsWith("android.support"))
		{
			ExceptionalUnitGraph graph = new ExceptionalUnitGraph(b);

			for (Unit unit : graph.getHeads())
			{
				traverse(b, graph, unit, new HashSet<Value>(), new HashSet<String>(), new HashSet<Unit>());
			}
		}
	}
	
	void traverse(Body b, ExceptionalUnitGraph graph, Unit unit, Set<Value> sdkIntValues, Set<String> conditions, Set<Unit> visitedUnits)
	{
		if (visitedUnits.contains(unit))
		{
			return;
		}
		else
		{
			visitedUnits.add(unit);
		}
		
		List<Unit> succUnits = null;
		Stmt stmt = (Stmt) unit;
		boolean sdkChecker = false;
		
		while(true)
		{
			if (stmt instanceof AssignStmt)
			{
				AssignStmt assignStmt = (AssignStmt) stmt;
				Value leftOp = assignStmt.getLeftOp();
				
				if (stmt.toString().contains("<android.os.Build$VERSION: int SDK_INT>"))
				{
					sdkIntValues.add(leftOp);
				}
				else
				{
					//Remove killed references
					if (sdkIntValues.contains(leftOp))
					{
						sdkIntValues.remove(leftOp);
					}
				}
			}
			
			if (stmt.containsInvokeExpr())
			{
				Edge edge = new Edge();
				edge.srcSig = b.getMethod().getSignature();
				edge.tgtSig = stmt.getInvokeExpr().getMethod().getSignature();
				edge.conditions = conditions.toString();
				
				ConditionalCallGraph.addEdge(edge);
				
				if (stmt.getInvokeExpr() instanceof InterfaceInvokeExpr)
				{
					SootMethod sootMethod = stmt.getInvokeExpr().getMethod();
					
					if (sootMethod.getDeclaration().toString().contains("private"))
					{
						//If the method is declared as private, then it cannot be extended by the sub-classes.
						continue;
					}
					
					SootClass sootClass = sootMethod.getDeclaringClass();
					Set<SootClass> subClasses = SootUtils.getAllSubClasses(sootClass);
					
					for (SootClass subClass : subClasses)
					{
						Edge e = new Edge();
						e.srcSig = edge.srcSig;
						e.conditions = edge.conditions;
						
						e.tgtSig = edge.tgtSig.replace(sootClass.getName() + ":", subClass.getName() + ":");
						
						ConditionalCallGraph.addEdge(e);
					}
				}
			}
			
			if (stmt instanceof IfStmt)
			{
				IfStmt ifStmt = (IfStmt) stmt;

				for (ValueBox vb : ifStmt.getCondition().getUseBoxes())
				{
					if (sdkIntValues.contains(vb.getValue()))
					{
						sdkChecker = true;
						break;
					}
				}
			}
			
			succUnits = graph.getSuccsOf(stmt);
			if (succUnits.size() == 1)
			{
				stmt = (Stmt) succUnits.get(0);
				
				if (visitedUnits.contains(stmt))
				{
					return;
				}
				else
				{
					visitedUnits.add(stmt);
				}
			}
			else
			{
				break;
			}
		}
		
		if (sdkChecker)
		{
			IfStmt ifStmt = (IfStmt) stmt;
			Stmt targetStmt = ifStmt.getTarget();
			
			Set<String> positiveConditions = cloneSet(conditions);
			positiveConditions.add(ifStmt.getCondition().toString());
			traverse(b, graph, targetStmt, sdkIntValues, positiveConditions, visitedUnits);
			
			succUnits.remove(targetStmt);
			Set<String> negativeConditions = cloneSet(conditions);
			negativeConditions.add("-" + ifStmt.getCondition().toString());
			for (Unit u : succUnits)
			{
				traverse(b, graph, u, sdkIntValues, negativeConditions, visitedUnits);
			}
		}
		else
		{
			for (Unit u : succUnits)
			{
				traverse(b, graph, u, sdkIntValues, conditions, visitedUnits);
			}
		}
	}
	
	public Set<String> cloneSet(Set<String> src)
	{
		Set<String> tgt = new HashSet<String>();
		for (String str : src)
		{
			tgt.add(str);
		}
		
		return tgt;
	}
}
