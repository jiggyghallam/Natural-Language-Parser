package common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formaliser {
	
	//String[] acceptanceList = {"nsubj", "prep","dobj"};
	
	public Formaliser() {
		
	}

	
	public void createRule(String rule, ArrayList<Dependency> dependencies, String[] acceptanceList) {
			ArrayList<Dependency> relatedToRule = new ArrayList<Dependency>();
			for (Dependency d: dependencies) {
				if (d.isRelationAccepted(acceptanceList)) {
					
				}
			}
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
