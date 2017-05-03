package lu.uni.snt.cid.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.util.Chain;

public class SootUtils 
{
	public static boolean isSuperClass(SootClass original, SootClass tested)
	{
		List<SootClass> workList = new ArrayList<SootClass>();
		
		if (original.hasSuperclass())
		{
			SootClass superCls = original.getSuperclass();
			
			if (! superCls.getName().equals("java.lang.Object"))
			{
				workList.add(original.getSuperclass());
			}
		}
		for (Iterator<SootClass> iter = original.getInterfaces().snapshotIterator(); iter.hasNext(); )
		{
			SootClass sc = iter.next();
			workList.add(sc);
		}
		
		while (! workList.isEmpty())
		{
			SootClass sc = workList.remove(0);
			
			if (sc.getName().equals(tested.getName()))
			{
				return true;
			}
			else
			{
				if (sc.hasSuperclass())
				{
					if (! sc.getSuperclass().getName().equals("java.lang.Object"))
					{
						workList.add(sc.getSuperclass());
					}
				}
				
				for (Iterator<SootClass> iter = sc.getInterfaces().snapshotIterator(); iter.hasNext(); )
				{
					workList.add(iter.next());
				}
			}
		}
		
		return false;
	}
	
	public static Set<SootClass> getAllSubClasses(SootClass sootClass)
	{
		Set<SootClass> sootClasses = new HashSet<SootClass>();
		
		Chain<SootClass> applicationClasses = Scene.v().getApplicationClasses();
		for (Iterator<SootClass> iter = applicationClasses.snapshotIterator(); iter.hasNext(); )
		{
			SootClass sc = iter.next();
			
			if (isSuperClass(sc, sootClass))
			{
				sootClasses.add(sc);
			}
		}
		
		return sootClasses;
	}
}
