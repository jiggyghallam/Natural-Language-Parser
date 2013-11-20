package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formaliser {
	
	String[] acceptanceList = {"nsubj", "prep","dobj"};
	
	public Formaliser() {
		
	}

	
	public void createFirstOrderSentance(String string) {
			
	}
	
	public void relatedToRoot(String string) {
		Pattern p = Pattern.compile("(?i)(root\\(ROOT-0,*?)(\\s)(.+?)(\\))");
		Matcher m = p.matcher(string);
		m.find();
		String rootLine = m.group();		
		String rootPattern = "(?i)(root\\(ROOT-0,*?)(\\s)(.+?)(\\))";
		String updated = string.replaceAll(rootPattern, "$3");
		rootLine = rootLine.replaceAll(rootPattern, "$3");
		
		System.out.println("========================Original Root Relations =======================");
		p = Pattern.compile("(?i)(.+?)("+rootLine+",*?)(\\s)(.+?)(\\))");
		m = p.matcher(string);
		while(m.find()) {
			System.out.println("Related to " + rootLine + " " + m.group());
		}
		System.out.println("========================Useful Root Relations =======================");
		for(int i = 0; i < acceptanceList.length; i++) {
			p = Pattern.compile("(?i)("+ acceptanceList[i] + ".*?)(\\("+rootLine+",*?)(\\s)(.+?)(\\))");
			m = p.matcher(string);
			while(m.find()) {
				System.out.println("Related to " + rootLine + " " + m.group());
			}
			
		}

		
		
		//System.out.println(rootLine);
		
		//System.out.println(updated);
		
	}
}
