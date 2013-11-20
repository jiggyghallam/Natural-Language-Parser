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
		
		Parser p = new Parser("models/englishPCFG.ser.gz", new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
		SentanceProcessor sp = new SentanceProcessor("models/englishPCFG.ser.gz", new String[]{"-outputFormat", "penn,typedDependenciesCollapsed", "-retainTmpSubcategories"});
		
		//Tree pSent = sp.processSentence("The trophy doesn't fit into the brown suitcase because it's too small.");
		Tree pSent = sp.processSentence("I poured water from the bottle into the cup until it was full.");
		
		//p.parseSentance(pSent);
		//p.printTree(pSent, "penn,typedDependenciesCollapsed");
		System.out.println("====================Original Tree==================");
		String s = p.getTreeDependencies(pSent);
		System.out.println(s);
		System.out.println("===================================================");
		System.out.println();
		Formaliser f = new Formaliser();
		System.out.println("====================Root Relations==================");
		f.relatedToRoot(s);
	}   
	
}
