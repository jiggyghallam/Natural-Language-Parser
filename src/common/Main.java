package common;

import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import java.io.StringReader;

import net.sf.extjwnl.JWNLException;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;

public class Main {

	private static String DICT_LOCATION = "dict/non_mapped/file_properties.xml";
	
	public static void main(String[] args) {
		syno_stuff();
	}
	
	private static void parser_stuff() {

		Parser p = new Parser("models/englishPCFG.ser.gz", new String[] {
				"-maxLength", "80", "-retainTmpSubcategories" });
		SentanceProcessor sp = new SentanceProcessor(
				"models/englishPCFG.ser.gz", new String[] { "-outputFormat",
						"penn,typedDependenciesCollapsed",
						"-retainTmpSubcategories" });

//		ArrayList<Tree> pSentArr = new ArrayList<Tree>();
//		pSentArr.add(sp
//				.processSentence("The trophy doesn't fit into the brown suitcase because it's too small."));
//		pSentArr.add(sp
//				.processSentence("I poured water from the bottle into the cup until it was full."));
//		pSentArr.add(sp
//				.processSentence("The man couldn't lift his son because he was so weak."));
//		pSentArr.add(sp
//				.processSentence("I took the water bottle out of the backpack so that it would be lighter."));
//		pSentArr.add(sp
//				.processSentence("The table won't fit through the doorway because it is too wide."));
//		pSentArr.add(sp
//				.processSentence("I can't cut that tree down with that axe; it is too thick."));
//		pSentArr.add(sp
//				.processSentence("The path to the lake was blocked, so we couldn't reach it."));
//		pSentArr.add(sp
//				.processSentence("The delivery truck zoomed by the school bus because it was going so fast."));
//		pSentArr.add(sp
//				.processSentence("John couldn't see the stage with Billy in front of him because he is so short."));
//		pSentArr.add(sp
//				.processSentence("Jane gave Kirsty some candy because she was hungry."));
//		pSentArr.add(sp
//		.processSentence("Tom threw his schoolbag down to Ray after he reached the top of the stairs."));
//		pSentArr.add(sp
//		.processSentence("The sculpture rolled off the shelf because it wasn't anchored."));


		Tree pSent = sp
				.processSentence("I took the water bottle out of the backpack so that it would be lighter.");
		System.out
				.println("====================Original Tree==================");

		String s = p.getTreeDependencies(pSent);
		System.out.println(s);

		System.out
				.println("===================================================");
		System.out.println();
		Formaliser f = new Formaliser();

		System.out
				.println("====================Root Relations==================");
		ArrayList<String> temp = f.getAllDependenciesRelations(s);
		ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
		for (String str : temp) {
			dependencies.add(new Dependency(str));
		}

		
		System.out
		.println("\n===================================================");
		String root = "";
		for (Dependency d : dependencies) {
			if (d.getNode_1_number() == 0) {
				root = d.getNode_2_string();
			}
			if(d.getNode_1_string().compareToIgnoreCase(root) == 0 || d.getNode_1_string().compareToIgnoreCase("reach") == 0)
			{
				System.out.println(d.toString());
			}
		}
		System.out
		.println("\n=======================rule=========================\n");
		f.createRule(root, dependencies,
				new String[] { "nsubj", "prep", "dobj", "iobj" }, false,true);
		
		System.out.println();
	}
	
	private static void syno_stuff() {
		SynonymGenerator sg = new SynonymGenerator(DICT_LOCATION);
		try {
			sg.getWord("dog");
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

