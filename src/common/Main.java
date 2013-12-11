package common;

import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import simplenlg.features.Tense;

import NLP.Dependency;
import NLP.Formaliser;
import NLP.Parser;
import NLP.TenseModifier;
import NLP.Word;
import NLP.WordNet;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;

public class Main {

	private static String WORD = "takes";
	private static String DICT_LOCATION = "dict/non_mapped/file_properties.xml";
	private static String PARSER_URL = "models/englishPCFG.ser.gz";
	private static String TAGGER_URL = "models/english-bidirectional-distsim.tagger";
	private static String SENTENCE1 = "The trophy doesn't fit into the brown suitcase because it's too small.";
	private static int HYPER_DEPTH = 8;
	private static int HYPO_DEPTH = 4;
	private static Map<String, Integer> hashMap;

	public static void main(String[] args) {
		hashMap = new HashMap<String, Integer>();
		// syno_stuff();
		// tense_stuff();
		// parser_stuff();
		combine(SENTENCE1);
	}

	private static void parser_stuff() {

		Parser p = new Parser(PARSER_URL, TAGGER_URL, new String[] {
				"-maxLength", "80", "-retainTmpSubcategories" });
		SentanceProcessor sp = new SentanceProcessor(PARSER_URL, new String[] {
				"-outputFormat", "penn,typedDependenciesCollapsed",
				"-retainTmpSubcategories" });

		// ArrayList<Tree> pSentArr = new ArrayList<Tree>();
		// pSentArr.add(sp
		// .processSentence("The trophy doesn't fit into the brown suitcase because it's too small."));
		// pSentArr.add(sp
		// .processSentence("I poured water from the bottle into the cup until it was full."));
		// pSentArr.add(sp
		// .processSentence("The man couldn't lift his son because he was so weak."));
		// pSentArr.add(sp
		// .processSentence("I took the water bottle out of the backpack so that it would be lighter."));
		// pSentArr.add(sp
		// .processSentence("The table won't fit through the doorway because it is too wide."));
		// pSentArr.add(sp
		// .processSentence("I can't cut that tree down with that axe; it is too thick."));
		// pSentArr.add(sp
		// .processSentence("The path to the lake was blocked, so we couldn't reach it."));
		// pSentArr.add(sp
		// .processSentence("The delivery truck zoomed by the school bus because it was going so fast."));
		// pSentArr.add(sp
		// .processSentence("John couldn't see the stage with Billy in front of him because he is so short."));
		// pSentArr.add(sp
		// .processSentence("Jane gave Kirsty some candy because she was hungry."));
		// pSentArr.add(sp
		// .processSentence("Tom threw his schoolbag down to Ray after he reached the top of the stairs."));
		// pSentArr.add(sp
		// .processSentence("The sculpture rolled off the shelf because it wasn't anchored."));

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
			if (d.getNode_1_string().compareToIgnoreCase(root) == 0
					|| d.getNode_1_string().compareToIgnoreCase("reach") == 0) {
				System.out.println(d.toString());
			}
		}
		System.out
				.println("\n=======================rule=========================\n");
		f.getRuleAsString(root, dependencies, new String[] { "nsubj", "prep",
				"dobj", "iobj" }, false, true);

		System.out.println();
		System.out.println();
		System.out
				.println("\n=======================tagger=========================\n");
		p.getPOSTaggedSentence("I took the water bottle out of the backpack so that it would be lighter.");
	}

	private static void syno_stuff() {
		WordNet sg = new WordNet(DICT_LOCATION);
		try {
			// sg.printDirectHypernym(WORD);
			// sg.printHypernymTreeAllPOS(WORD, 20);
			// sg.printHypernymTree(WORD, 20, POS.VERB);
			// sg.printHyponym(WORD, POS.NOUN);
			IndexWord iw = sg.getStringAsIndexWord(WORD, POS.VERB);
			System.out.println(iw.getLemma());
			// sg.printHyponym(WORD);
			// sg.printSynonyms(WORD);
			// sg.printSynonymTreeAllPOS(WORD, 2);
			// sg.printInfo("run", POS.VERB);
			// sg.printAsymmetricRelationship("dog", "cat", 1);
			sg.printRelationship("walk", "stdsep", 1, PointerType.ENTAILMENT);
		} catch (JWNLException e) {
			System.out.println("Thrown");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void combine(String inputSentence) {
		ArrayList<String> pStringsArray = new ArrayList<String>();
		pStringsArray
				.add("The trophy doesn't fit into the brown suitcase because it's too small.");
		pStringsArray
				.add("I poured water from the bottle into the cup until it was full.");
		pStringsArray
				.add("The man couldn't lift his son because he was so weak.");
		pStringsArray
				.add("I took the water bottle out of the backpack so that it would be lighter.");
		pStringsArray
				.add("The table won't fit through the doorway because it is too wide.");
		pStringsArray
				.add("I can't cut that tree down with that axe; it is too thick.");
		pStringsArray
				.add("The path to the lake was blocked, so we couldn't reach it.");
		pStringsArray
				.add("The delivery truck zoomed by the school bus because it was going so fast.");
		pStringsArray
				.add("John couldn't see the stage with Billy in front of him because he is so short.");
		pStringsArray
				.add("Jane gave Kirsty some candy because she was hungry.");
		pStringsArray
				.add("Tom threw his schoolbag down to Ray after he reached the top of the stairs.");
		pStringsArray
				.add("The sculpture rolled off the shelf because it wasn't anchored.");

		Formaliser f = new Formaliser();
		File file = new File("wordinfo.tptp");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		WordNet wordNet = new WordNet(DICT_LOCATION);
		TenseModifier tenseModifier = new TenseModifier();
		String root = "";
		Dependency rootDependency = null;
		Parser p = new Parser(PARSER_URL, TAGGER_URL, new String[] {
				"-maxLength", "80", "-retainTmpSubcategories" });
		SentanceProcessor sp = new SentanceProcessor(PARSER_URL, new String[] {
				"-outputFormat", "penn,typedDependenciesCollapsed",
				"-retainTmpSubcategories" });

		for (String input : pStringsArray) {
			System.out.println();
			System.out.println(input);
			Tree pSent = sp.processSentence(input);
			String s = p.getTreeDependencies(pSent);
			ArrayList<String> temp = f.getAllDependenciesRelations(s);
			ArrayList<Dependency> dependencies = new ArrayList<Dependency>();

			for (String str : temp) {
				dependencies.add(new Dependency(str));
			}
			for (Dependency d : dependencies) {
				if (d.getNode_1_number() == 0) {
					root = d.getNode_2_string();
					rootDependency = d;
					break;
				}
			}
			ArrayList<Dependency> dependenciesRelatedToRule = new ArrayList<Dependency>();
			dependenciesRelatedToRule.add(rootDependency);
			dependenciesRelatedToRule.addAll(f.getRuleDependencies(root,
					dependencies, new String[] { "nsubj", "prep", "dobj",
							"iobj" }, false, false));

			String taggedSentence = p.getPOSTaggedSentence(input);

			ArrayList<Word> ruleWords = new ArrayList<Word>();
			for (Dependency d : dependenciesRelatedToRule) {
				ruleWords.add(p.getWord(d, taggedSentence, wordNet));
			}

			try {
				for (Word w : ruleWords) {
					if (!w.isPronoun()) {
						w.setString(tenseModifier.changeTense(w.getString(),
								Tense.PRESENT));
						w.setHyponymTreeArray(wordNet.getHyponymTree(
								w.getString(), w.getPos(), HYPO_DEPTH));
						w.setHypernymTreeArray(wordNet.getHypernymTree(
								w.getString(), HYPER_DEPTH, w.getPos()));
						w.setInfo(wordNet.getInfo(w.getString(), w.getPos()));
						w.setSynonyms(wordNet.getSynonymTree(w.getString(), 5,
								w.getPos()));
					}
					// w.print();
				}
				System.out
						.println("\n\n\n\n====================================================================");

				for (Word w : ruleWords) {
					w.compileWord(true);
					// bw.append(w.getCompiledInfo());
					String compiledString = w.getCompiledInfo();
					String[] compiledStringArray = compiledString.split("\\n");
					for (String str : compiledStringArray) {
							Integer val = hashMap.get(str);
							if (val == null) {
								val = new Integer(0);
								bw.append(str + "\n");
							}
							hashMap.put(str, val);
					}
					bw.append("\n");

					bw.flush();
				}

				System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JWNLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void tense_stuff() {
		TenseModifier tm = new TenseModifier();
		System.out.println(tm.changeTense(WORD, Tense.PRESENT));
	}
}
