package com.p6majo.math.newtoniteration;


import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Engine;
import com.maplesoft.openmaple.EngineCallBacksDefault;
import com.p6majo.math.complex.Complex;
import com.p6majo.math.function.MapleZetaOverZetaPrime;
import com.p6majo.math.utils.Box;

import java.util.TreeSet;

public class IterationZeta {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(100,3000);
		Box box = new Box(0,0,1,30);
		String[] a=new String[]{"java"};
		
		
		try{
			
			Engine engine = new Engine( a, new EngineCallBacksDefault(), null, null );
			MapleZetaOverZetaPrime f = new MapleZetaOverZetaPrime(engine);
			NewtonMapleIterator iterator = new NewtonMapleIterator(f,box,res,String.format("zeta2.ppm"));
			
			TreeSet<Complex> roots = new TreeSet<Complex>();
			//add roots
			for (int i=-2;i>-100;i-=2) 	roots.add(new Complex(i,0));
	
			
			double[] nontrivial = new double[]{
				14.13472514173469379045725198356247027078425711569924317568556746014,
				21.022039638771554992628479593896902777334340524902781754629520403587,
				25.010857580145688763213790992562821818659549672557996672496542006745,
				30.424876125859513210311897530584091320181560023715440180962146036993,
				32.935061587739189690662368964074903488812715603517039009280003440784,
				37.586178158825671257217763480705332821405597350830793218333001113622,
				40.918719012147495187398126914633254395726165962777279536161303667253,
				43.327073280914999519496122165406805782645668371836871446878893685521,
				48.005150881167159727942472749427516041686844001144425117775312519814,
				52.970321477714460644147296608880990063825017888821224779900748140317,
				49.773832477672302181916784678563724057723178299676662100781955750433	
			};
			
			for (int i=0;i<nontrivial.length;i++) {roots.add(new Complex(0.5,nontrivial[i]));roots.add(new Complex(0.5,-nontrivial[i]));};
			
			
			iterator.setRoots(roots);
			//SSystem.out.println(iterator.getRoots().toString());
			iterator.generateImage();
		}
		catch(MapleException e){
			System.out.println("Something went wrong "+e.getMessage());
		}
		
		
		System.out.println(System.getProperty("java.class.path"));

	}
}
