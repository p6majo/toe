package com.p6majo.math.newtoniteration;


import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Engine;
import com.maplesoft.openmaple.EngineCallBacksDefault;
import com.p6majo.math.function.MapleNegOneOverPsiFunction;
import com.p6majo.math.utils.Box;
import com.p6majo.math.utils.Resolution;

public class IterationOneOverGamma {
	
	public static void main(String[] args){
		
		Resolution res = new Resolution(1200,800);
		Box box = new Box(-5,-2,1,2);
		String[] a=new String[]{"java"};
		
		try{
			Engine engine = new Engine( a, new EngineCallBacksDefault(), null, null );
			MapleNegOneOverPsiFunction psi = new MapleNegOneOverPsiFunction(engine);
			NewtonMapleIterator iterator = new NewtonMapleIterator(psi,box,res,String.format("tmp.ppm"));
			iterator.generateRoots(100);
			System.out.println(iterator.getRoots());
			iterator.generateImage();
		}
		catch(MapleException e){
			System.out.println("Something went wrong "+e.getMessage());
		}

	}
}
