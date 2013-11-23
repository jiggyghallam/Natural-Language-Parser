package common;

import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import java.io.StringReader;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;

public class Main {

	public static void main(String[] args) {

		Parser p = new Parser("models/englishPCFG.ser.gz", new String[] {
				"-maxLength", "80", "-retainTmpSubcategories" });
		SentanceProcessor sp = new SentanceProcessor(
				"models/englishPCFG.ser.gz", new String[] { "-outputFormat",
						"penn,typedDependenciesCollapsed",
						"-retainTmpSubcategories" });
		/*
		 * ArrayList<Tree> pSentArr = new ArrayList<Tree>(); pSentArr.add(sp
		 * .processSentence
		 * ("The trophy doesn't fit into the brown suitcase because it's too small."
		 * )); pSentArr.add(sp .processSentence(
		 * "I poured water from the bottle into the cup until it was full."));
		 * pSentArr.add(sp
		 * .processSentence("The man couldn't lift his son because he was so weak."
		 * )); pSentArr.add(sp .processSentence(
		 * "I took the water bottle out of the backpack so that it would be lighter."
		 * )); // Tree pSent = // sp.processSentence(
		 * "I was trying to balance the bottle upside down on the table, but I couldn't do it because it was so top-heavy."
		 * );
		 * 
		 * // p.parseSentance(pSent); // p.printTree(pSent,
		 * "penn,typedDependenciesCollapsed"); for (Tree pSent : pSentArr) {
		 * System.out
		 * .println("====================Original Tree==================");
		 * String s = p.getTreeDependencies(pSent); System.out.println(s);
		 * System.out
		 * .println("===================================================");
		 * System.out.println(); Formaliser f = new Formaliser();
		 * 
		 * System.out
		 * .println("====================Root Relations==================");
		 * //f.relatedToRoot(s); ArrayList<String> temp =
		 * f.getAllDependenciesRelations(s); Dependency d = new
		 * Dependency(temp.get(0)); }
		 */
		Tree pSent = sp
				.processSentence("I poured water from the bottle into the cup until it was full.");
		System.out.println("====================Original Tree==================");
		
		String s = p.getTreeDependencies(pSent);
		System.out.println(s);
		
		System.out.println("===================================================");
		System.out.println();
		Formaliser f = new Formaliser();
		
		System.out.println("====================Root Relations==================");
		ArrayList<String> temp = f.getAllDependenciesRelations(s);
		ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
		for(String str: temp) {
			dependencies.add(new Dependency(str));
		}
		
		for(Dependency d: dependencies) {
			if (d.getRelation().startsWith("prep")) {
				System.out.println(d.toString());
			}
		}

	}

}
