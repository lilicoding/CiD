package lu.uni.snt.cid.ccg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.snt.cid.utils.MethodSignature;

public class ConditionalCallGraph 
{
	//public static Map<String, Set<Edge>> srcMethod2edges = new HashMap<String, Set<Edge>>();
	public static Map<String, Set<Edge>> tgtMethod2edges = new HashMap<String, Set<Edge>>();
	public static Map<String, Set<String>> cls2methods = new HashMap<String, Set<String>>();
	
	public static Map<String, Edge> existingEdges = new HashMap<String, Edge>();
	
	public static void addEdge(Edge edge)
	{
		if (edge.srcSig.isEmpty() || edge.tgtSig.isEmpty())
		{
			return;
		}
		
		if (edge.srcSig.equals(edge.tgtSig))
		{
			return;
		}
		
		/*
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
		}*/
		
		
		
		//We check tgtMethod2edges only because we will only leverage this map for traversing the call graph
		/*if (! causeCircle(edge))
		{
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
		}*/
		
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
		
		if (! edge.srcSig.contains("<init>"))
		{
			String cls = new MethodSignature(edge.srcSig).getCls();
			if (cls2methods.containsKey(cls))
			{
				Set<String> methods = cls2methods.get(cls);
				if (null == methods)
				{
					methods = new HashSet<String>();
				}
				methods.add(edge.srcSig);
				cls2methods.put(cls, methods);
			}
			else
			{
				Set<String> methods = new HashSet<String>();
				methods.add(edge.srcSig);
				cls2methods.put(cls, methods);
			}
		}
		
		if (! edge.tgtSig.contains("<init>"))
		{
			String cls = new MethodSignature(edge.tgtSig).getCls();
			if (cls2methods.containsKey(cls))
			{
				Set<String> methods = cls2methods.get(cls);
				if (null == methods)
				{
					methods = new HashSet<String>();
				}
				methods.add(edge.tgtSig);
				cls2methods.put(cls, methods);
			}
			else
			{
				Set<String> methods = new HashSet<String>();
				methods.add(edge.tgtSig);
				cls2methods.put(cls, methods);
			}
		}
	}
	
	public static Edge getEdge(String srcSig, String tgtSig)
	{
		String key = srcSig + "/" + tgtSig;
		if (existingEdges.containsKey(key))
		{
			return existingEdges.get(key);
		}
		else
		{
			Edge edge = new Edge();
			edge.srcSig = srcSig;
			edge.tgtSig = tgtSig;
			
			existingEdges.put(key, edge);
			
			return edge;
		}
	}
	
	public static void expandConstructors()
	{
		Set<String> initMethods = new HashSet<String>();
		
		for (String method : tgtMethod2edges.keySet())
		{
			if (method.contains("<init>"))
			{
				initMethods.add(method);
			}
		}
		
		for (String method : initMethods)
		{
			String cls = new MethodSignature(method).getCls();
			
			if (cls2methods.containsKey(cls))
			{
				Set<String> methods = cls2methods.get(cls);
				for (String m : methods)
				{
					Edge edge = new Edge();
					edge.srcSig = method;
					edge.tgtSig = m;
					
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
			}
		}
	}
	
	public static boolean causeCircle(Edge edge)
	{
		Set<String> visitedMethods = new HashSet<String>();
		visitedMethods.add(edge.tgtSig);
		
		List<Edge> workList = new ArrayList<Edge>();
		
		if (tgtMethod2edges.containsKey(edge.tgtSig))
		{
			Set<Edge> edges = tgtMethod2edges.get(edge.tgtSig);
			if (null != edges)
			{
				for (Edge e : edges)
				{
					workList.add(e);
				}
			}
		}	
		
		while (! workList.isEmpty())
		{
			Edge e = workList.remove(0);
			
			if (visitedMethods.contains(e.srcSig))
			{
				return true;
			}
			else
			{
				visitedMethods.add(e.srcSig);
				
				Set<Edge> edges = tgtMethod2edges.get(e.srcSig);
				if (null != edges)
				{
					workList.addAll(edges);
				}
			}
		}
		
		return false;
	}
	
	
	public static List<String> obtainConditions(String methodSig)
	{
		List<String> conditions = new ArrayList<String>();
		
		if (! tgtMethod2edges.containsKey(methodSig))
		{
			return conditions;
		}
		
		Set<Edge> edges = tgtMethod2edges.get(methodSig);
		
		List<Edge> workList = new ArrayList<Edge>();
		workList.addAll(edges);
		
		Set<Edge> visitedEdges = new HashSet<Edge>();
		
		while (! workList.isEmpty())
		{
			Edge e = workList.remove(0);
			visitedEdges.add(e);

			String cond = e.conditions.toString().replaceAll("\\[", "").replaceAll("]", "");
			
			if (! cond.isEmpty())
			{
				conditions.add(e.conditions.toString());
			}
			
			edges = tgtMethod2edges.get(e.srcSig);
			
			if (null != edges)
			{
				for (Edge edge : edges)
				{
					if (! visitedEdges.contains(edge))
					{
						workList.add(edge);
					}
				}
			}
				
		}
		
		return conditions;
	}
	
	public static Set<String> visitedCalls = null;
	
	public static List<String> obtainCallStack(String methodSig)
	{
		List<String> callStack = new ArrayList<String>();
		callStack.add(methodSig + "\n");
		
		String arrow = "> ";
		
		visitedCalls = new HashSet<String>();
		visitedCalls.add(methodSig);
		
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
		//Circle found, Stop here
		if (visitedCalls.contains(edge.srcSig))
		{
			return callStack;
		}
		else
		{
			visitedCalls.add(edge.srcSig);
		}
		
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
