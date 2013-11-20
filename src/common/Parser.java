package common;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class Parser {

	private String fileURL;
	private LexicalizedParser lp;

	public Parser(String fileURL, String[] options) {
		this.fileURL = fileURL;
		lp = LexicalizedParser.loadModel(fileURL);
		lp.setOptionFlags(options);
	}

	public void parseSentance(Tree sentance) {
		Tree parse = sentance;
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		System.out.println(tdl);
		System.out.println();


	}
	
	public void printTree(Tree tree, String options) {
		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		tp.printTree(tree);
	}
	
	public String getTreeDependencies(Tree tree) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(baos);
	    PrintStream old = System.out;
	    System.setOut(ps);
		TreePrint tp = new TreePrint("typedDependenciesCollapsed");
		PrintWriter pw = new PrintWriter(System.out, true);
		tp.printTree(tree, pw);
		
	    System.out.flush();
	    System.setOut(old);
	    
	    return baos.toString();
	}

}