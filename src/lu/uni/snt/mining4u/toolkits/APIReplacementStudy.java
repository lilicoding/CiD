package lu.uni.snt.mining4u.toolkits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.mining4u.AndroidClasses;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class APIReplacementStudy extends BodyTransformer 
{
	public static void main(String[] args) 
	{
		APIReplacementStudy arStudy = new APIReplacementStudy();
		arStudy.run("/Volumes/joey/workspace/mudflow_benign_apps/FE6C09CBADF37070A65F168CCEE960B861BBD62750818037AEECCF10AEBE9980.apk", 
				"/Volumes/joey/li/github/android-platforms");
		
		for (String item : arStudy.apiReplacements)
		{
			System.out.println(item);
		}
	}

	public List<String> apiReplacements = new ArrayList<String>();
	
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
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.APIReplacementStudy", this));
		soot.Main.main(args);
		
		G.reset();
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		if (b.toString().contains("<android.os.Build$VERSION: int SDK_INT>"))
		{
			ExceptionalUnitGraph graph = new ExceptionalUnitGraph(b);
			PatchingChain<Unit> units = b.getUnits();
			
			System.out.println(b);
			
			for (Iterator<Unit> unitIter = units.snapshotIterator(); unitIter.hasNext(); )
			{
				Stmt stmt = (Stmt) unitIter.next();
				
				if (stmt.toString().contains("<android.os.Build$VERSION: int SDK_INT>"))
				{
					//It should be only one succeed statement and the statement should be an if-statement
					List<Unit> succUnits = graph.getSuccsOf(stmt);
					if (succUnits.size() != 1)
					{
						continue;
					}
					
					Stmt succStmt = (Stmt) succUnits.get(0);
					
					if (succStmt instanceof IfStmt)
					{
						IfStmt ifStmt = (IfStmt) succStmt;
						
						visitIfStmt(ifStmt, graph);
					}
				}
			}
		}
	}
	
	public void visitIfStmt(IfStmt ifStmt, ExceptionalUnitGraph graph)
	{
		List<Unit> succUnits = graph.getSuccsOf(ifStmt);
		
		if (succUnits.size() != 2)
		{
			return;
		}
		
		Stmt targetStmt = ifStmt.getTarget();
		
		Set<String> originalAPIs = new HashSet<String>();
		Set<String> replacedAPIs = new HashSet<String>();
		
		for (Unit unit : succUnits)
		{
			Stmt stmt = (Stmt) unit;
			
			if (! stmt.toString().equals(targetStmt.toString())) //original
			{
				visitStmt(stmt, graph, originalAPIs, new HashSet<Unit>());
			}
			else //replacement
			{
				visitStmt(stmt, graph, replacedAPIs, new HashSet<Unit>());
			}
		}
		
		Set<String> commonAPIs = new HashSet<String>(originalAPIs);
		commonAPIs.retainAll(replacedAPIs);
		
		originalAPIs.removeAll(commonAPIs);
		replacedAPIs.removeAll(commonAPIs);
		
		for (String api : originalAPIs)
		{
			apiReplacements.add(api + "-->" + toUniqueStr(replacedAPIs));
		}
	}
	
	public String toUniqueStr(Set<String> set)
	{
		if (set.isEmpty())
		{
			return "";
		}
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		StringBuilder sb = new StringBuilder();
		for (String str : list)
		{
			sb.append(str + "|");
		}
		
		String str = sb.toString();
		
		return str.substring(0, str.length());
	}
	
	public void visitStmt(Stmt stmt, ExceptionalUnitGraph graph, Set<String> androidAPIs, Set<Unit> visitedStmts)
	{
		visitedStmts.add(stmt);
		if (stmt.containsInvokeExpr())
		{
			String clsName = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
			if (AndroidClasses.isAndroidClass(clsName))
			{
				androidAPIs.add(stmt.getInvokeExpr().getMethod().getSignature());
			}
		}
		else if (stmt.containsFieldRef())
		{
			String clsName = stmt.getFieldRef().getField().getDeclaringClass().getName();
			if (AndroidClasses.isAndroidClass(clsName))
			{
				androidAPIs.add(stmt.getFieldRef().getField().getSignature());
			}
		}
		
		List<Unit> units = graph.getSuccsOf(stmt);
		for (Unit unit : units)
		{
			if (visitedStmts.contains(unit))
			{
				continue;
			}
			else
			{
				visitStmt((Stmt) unit, graph, androidAPIs, visitedStmts);
			}
		}
	}
}
