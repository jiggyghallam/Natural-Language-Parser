package common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Formaliser {
	
	//String[] acceptanceList = {"nsubj", "prep","dobj"};
	
	public Formaliser() {
		
	}

	
	public String createRule(String rule, ArrayList<Dependency> dependencies, String[] acceptanceList, boolean acceptPersonalPronouns, boolean acceptNounCompoundModifiers) {
			boolean negation = false;
			ArrayList<Dependency> relatedToRule = new ArrayList<Dependency>();
			for (Dependency d: dependencies) {
				if (d.isRelationAccepted(acceptanceList)) {
					relatedToRule.add(d);
				}
			}
			
			for (Dependency d: dependencies) {
				if (d.isRelationAccepted(new String[] {"neg"}) && d.getNode_1_string().compareToIgnoreCase(rule) == 0) {
					negation = true;
				}
			}
			
			if (!acceptPersonalPronouns) {
				for (Iterator<Dependency> iterator = relatedToRule.iterator(); iterator.hasNext();) {
					Dependency d = iterator.next();
					if (d.isNode1PersonalPronoun() || d.isNode2PersonalPronoun()) 
						iterator.remove();
				}
			}
			
			if (acceptNounCompoundModifiers) {
				for (Dependency d:dependencies) {
					if (d.isRelationAccepted(new String[] {"nn"})) {
						for (Dependency relatedD: relatedToRule) {
							if (relatedD.getNode_2_number() == d.getNode_1_number()) {
								relatedD.setNode_2_string(d.getNode_2_string()+ "-" + relatedD.getNode_2_string());
							}
						}
					}
				}
			}
			
			if (negation) {
				System.out.print("not ");
			}
			System.out.print(rule + "(");
			for (Dependency d: relatedToRule) {
				System.out.print(d.getNode_2_string() + " ");				
			}
			System.out.print(")");
			return "";
	}
	
		
	public ArrayList<String> getAllDependenciesRelations(String string) {
		Pattern p;
		Matcher m;
		ArrayList<String> relations = new ArrayList<String>();
		p = Pattern.compile("(?i)(.*?)(\\(.+?)(\\s)(.+?)(\\))");
		m = p.matcher(string);
		while(m.find()) {
			relations.add(m.group());
		}
			
		
		return relations;
		
	}
	
	public ArrayList<String> getDependenciesRelations(String string, String[] depenencies) {
		Pattern p;
		Matcher m;
		ArrayList<String> relations = new ArrayList<String>();
		for(int i = 0; i < depenencies.length; i++) {
			p = Pattern.compile("(?i)("+ depenencies[i] + ".*?)(\\(.+?)(\\s)(.+?)(\\))");
			m = p.matcher(string);
			while(m.find()) {
				relations.add(m.group());
			}
			
		}
		
		return relations;
	}
	
	

}
