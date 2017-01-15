package lu.uni.snt.mining4u.ari;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConditionalCallGraph 
{
	public static Map<String, Set<Edge>> srcMethod2edges = new HashMap<String, Set<Edge>>();
	public static Map<String, Set<Edge>> tgtMethod2edges = new HashMap<String, Set<Edge>>();
	
	public static void addEdge(Edge edge)
	{
		if (srcMethod2edges.containsKey(edge.srcSig))
		{
			Set<Edge> srcEdges = srcMethod2edges.get(edge.srcSig);
			srcEdges.add(edge);
			srcMethod2edges.put(edge.srcSig, srcEdges);
		}
		else
		{
			Set<Edge> srcEdges = new HashSet<Edge>();
			srcEdges.add(edge);
			srcMethod2edges.put(edge.srcSig, srcEdges);
		}
		
		if (tgtMethod2edges.containsKey(edge.tgtSig))
		{
			Set<Edge> tgtEdges = tgtMethod2edges.get(edge.tgtSig);
			tgtEdges.add(edge);
			tgtMethod2edges.put(edge.tgtSig, tgtEdges);
		}
		else
		{
			Set<Edge> tgtEdges = new HashSet<Edge>();
			tgtEdges.add(edge);
			tgtMethod2edges.put(edge.tgtSig, tgtEdges);
		}
	}
	
	public static List<String> obtainConditions(String methodSig)
	{
		List<String> conditions = new ArrayList<String>();
		
		Set<Edge> edges = tgtMethod2edges.get(methodSig);
		
		List<Edge> workList = new ArrayList<Edge>();
		workList.addAll(edges);
		
		while (! workList.isEmpty())
		{
			Edge e = workList.remove(0);
			
			//System.out.println(e);
			
			if (! e.conditions.equals("[]"))
			{
				conditions.add(e.conditions);
			}
			
			edges = tgtMethod2edges.get(e.srcSig);
			
			if (null != edges)
				workList.addAll(edges);
		}
		
		return conditions;
	}
	
	public static List<String> obtainCallStack(String methodSig)
	{
		List<String> callStack = new ArrayList<String>();
		callStack.add(methodSig + "\n");
		
		String arrow = "> ";
		
		if (null != tgtMethod2edges.get(methodSig))
		{
			for (Edge e : tgtMethod2edges.get(methodSig))
			{
				obtainCallStack(callStack, "--" + arrow, e);
			}
		}

		return callStack;
	}
	
	public static List<String> obtainCallStack(List<String> callStack, String arrow, Edge edge)
	{
		callStack.add("|" + arrow + edge.srcSig + " " + edge.conditions + "\n");
		
		if (null != tgtMethod2edges.get(edge.srcSig))
		{
			for (Edge e : tgtMethod2edges.get(edge.srcSig))
			{
				obtainCallStack(callStack, "--" + arrow, e);
			}
		}
		
		return callStack;
	}
}
