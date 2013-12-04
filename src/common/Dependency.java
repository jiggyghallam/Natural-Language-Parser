package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enums.PersonalPronouns;

public class Dependency {
	
	private int node_1_number;
	private int node_2_number;
	private String relation;
	private String node_1_string;
	private String node_2_string;
	
	public Dependency() {}
	
	public Dependency(String dependency) {
		String regex = "(\\D+)\\((\\D+)-(\\d+), (\\D+)-(\\d+)\\)";
		Matcher m = Pattern.compile(regex).matcher(dependency);
		m.matches();
		setRelation(m.group(1));
		setNode_1_string(m.group(2));
		setNode_1_number(Integer.parseInt(m.group(3)));
		setNode_2_string(m.group(4));
		setNode_2_number(Integer.parseInt(m.group(5)));

	}

	@Override
	public String toString() {
		return (relation+"("+node_1_string+"-"+node_1_number+", "+node_2_string+"-"+node_2_number+")");
	}
	
	
	public boolean isRelationAccepted(String [] acceptenceList) {
		for (String element: acceptenceList){
			if (relation.startsWith(element))
				return true;
		}
		return false;
	}
	
	public boolean isNode2PersonalPronoun() {
		for(PersonalPronouns p: PersonalPronouns.values()) {
			if(node_2_string.compareToIgnoreCase(p.toString())==0)
				return true;
		}
		return false;
	}
	
	public boolean isNode1PersonalPronoun() {
		for(PersonalPronouns p: PersonalPronouns.values()) {
			if(node_1_string.compareToIgnoreCase(p.toString())==0)
				return true;
		}
		return false;
	}
	
	public int getNode_1_number() {
		return node_1_number;
	}

	public void setNode_1_number(int node_1_number) {
		this.node_1_number = node_1_number;
	}

	public int getNode_2_number() {
		return node_2_number;
	}

	public void setNode_2_number(int node_2_number) {
		this.node_2_number = node_2_number;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getNode_1_string() {
		return node_1_string;
	}

	public void setNode_1_string(String node_1_string) {
		this.node_1_string = node_1_string;
	}

	public String getNode_2_string() {
		return node_2_string;
	}

	public void setNode_2_string(String node_2_string) {
		this.node_2_string = node_2_string;
	}

}
