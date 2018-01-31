package com.p6majo.math.utils;

import java.util.ArrayList;
import java.util.Arrays;

public interface Params {
	
	public static enum XMLTag {group,name,dim,conjugacyclass,sub,elements,generators,map,automorphisms,label};
	public static enum XMLPolytopeTag {polytope,group,label,rank,vertices,vertex,coordinates,id,tag,planes,plane,codimension,parent,equation,normal,indices};

	
	//maximal list size (10GB)
	int MAXLISTCOUNT = 100000000;

	//how many elements are shown in the output at most
	int MAXOUTPUTCOUNT = 1000;
	
	//maximum size of the group catalog
	int MAXGROUPORDER =350;
	//should be larger than MAXGROUPORDER;
	int MAXPRIMENUMBER = 5000000;
	//Path for group data
	public static final String PATH = "data/";
	
	//number of possible actions that can be performed from the gui
	public static enum Actions { elementDetails, orderTree, generate, table, subgroups, isomorphism, subgrouplabeling,subgroupDetails, productDetails,groupcatalog,solvable,test}
	/*number of possible actions from the gui*/
	public final static int nActions = Actions.values().length;

	/*Series of finite groups that are used to identify unknown groups*/
	/* product has to be last in the enum otherwise groups behind product will be excluded from product groups*/
	public static enum Series{cyclic,dicyclic,alternating,symmetric,gl,sl,O,SO,glc,slc,U,SU,sp,dieder,gla,sla,abelian,gd,sd,mc,CoxA,CoxB,CoxD,CoxE,CoxF,CoxG,CoxH,AltCoxA,AltCoxB,AltCoxD,AltCoxE,AltCoxF,AltCoxH,GO,product} 
	
	
	public static final String SEPARATOR_GROUPLABEL = ";";
	public static final String SEPARATOR_FILEDATA = ";";
	public final static ArrayList<String> groupOrdering= new ArrayList<String>(
		    Arrays.asList("C", "A", "S","Dic","Gl","GlC","GlA","Sl","SlC","SlA","O","SO","U","SU","SP","D","GD","SD","MC","R","CoA","CoB","CoD","CoE","CoF","CoH","AltCoA","AltCoB","AltCoD","AltCoE","AltCoF","AltCoH","X"));

	
	public boolean LOGGING = false;
	
	/**
	 * Returns a String describing the action
	 * @param action
	 * @return
	 */
	public static String ActionString(Actions action){
		switch(action){
		case subgroups:
			return "Find all subgroups";
		case isomorphism:
			return "Check isomorphism to group ";
		case subgrouplabeling:
			return "Identify the type of subgroups: ";
		case subgroupDetails:
			return "Details for subgroup: ";
		case productDetails:
			return "Calculate all products with details:";
		case table:
			return "Calculate group table";
		case orderTree:
			return "List the order of all elements";
		case elementDetails:
			return "Show details of all elements";
		case solvable:
			return "Sequence of commutator groups";
		case groupcatalog:
			return "Show group catalog up to order ";
		case generate:
			return "Enter set of generators, e.g. 2,3 ";
		case test:
			return "Test results";
		default:
			return "";
		}
	}

	

}
