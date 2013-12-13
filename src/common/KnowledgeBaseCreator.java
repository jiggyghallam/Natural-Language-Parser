package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.extjwnl.JWNLException;
import simplenlg.features.Tense;
import edu.stanford.nlp.trees.Tree;
import NLP.Dependency;
import NLP.Formaliser;
import NLP.Parser;
import NLP.TenseModifier;
import NLP.Word;
import NLP.WordNet;

public class KnowledgeBaseCreator {

	private static String DICT_LOCATION = "dict/non_mapped/file_properties.xml";
	private static String PARSER_URL = "models/englishPCFG.ser.gz";
	private static String TAGGER_URL = "models/english-bidirectional-distsim.tagger";

	private String sentence;
	private String question;
	private String answer;
	private Formaliser f;
	private File file;
	private WordNet wordNet;
	private TenseModifier tenseModifier;
	private Parser p;
	private SentanceProcessor sp;
	private Map<String, Integer> hashMap;
	private StringBuilder stringBuilder;

	public KnowledgeBaseCreator() {
		f = new Formaliser();
		file = new File("wordinfo.tptp");
		wordNet = new WordNet(DICT_LOCATION);
		tenseModifier = new TenseModifier();
		p = new Parser(PARSER_URL, TAGGER_URL, new String[] { "-maxLength",
				"80", "-retainTmpSubcategories" });
		sp = new SentanceProcessor(PARSER_URL, new String[] { "-outputFormat",
				"penn,typedDependenciesCollapsed", "-retainTmpSubcategories" });
		hashMap = new HashMap<String, Integer>();
		stringBuilder = new StringBuilder();
	}

	/**
	 * Writes the current schema to file
	 * 
	 * @return <code>true</code> if the file is wrote successfully
	 * @throws IOException
	 */
	public boolean writeKnowledgeBaseToFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile()));
		if (!file.exists()) {
			file.createNewFile();
		}

		return false;
	}

	/**
	 * Processes the sentence part of a schema question
	 * @param sentence
	 * @param hyponymDpeth
	 * @param hypernymDepth
	 * @param multipleSenses allows the use of different senses of the word to be processed
	 * @return <code>true</code> if the sentence was processed
	 */
	public boolean processSentence(String sentence, int hyponymDpeth,
			int hypernymDepth, boolean multipleSenses) {
		this.sentence = sentence;
		String root = "";
		Dependency rootDependency = null;
		Tree pSent = sp.processSentence(sentence);
		String s = p.getTreeDependencies(pSent);

		ArrayList<String> temp = f.getAllDependenciesRelations(s);
		ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
		ArrayList<Dependency> dependenciesRelatedToRule = new ArrayList<Dependency>();
		ArrayList<Word> ruleWords = new ArrayList<Word>();

		String taggedSentence = p.getPOSTaggedSentence(sentence);

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

		dependenciesRelatedToRule.add(rootDependency);
		dependenciesRelatedToRule.addAll(f.getRuleDependencies(root,
				dependencies, new String[] { "nsubj", "prep", "dobj", "iobj" },
				false, false));

		for (Dependency d : dependenciesRelatedToRule) {
			ruleWords.add(p.getWord(d, taggedSentence, wordNet));
		}

		try {
			for (Word w : ruleWords) {
				if (!w.isPronoun()) {
					w.setString(tenseModifier.changeTense(w.getString(),
							Tense.PRESENT));
					w.setHyponymTreeArray(wordNet.getHyponymTree(w.getString(),
							w.getPos(), hyponymDpeth));
					w.setHypernymTreeArray(wordNet.getHypernymTree(
							w.getString(), hypernymDepth, w.getPos()));
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
						stringBuilder.append(str + "\n");
					}
					hashMap.put(str, val);
				}
				stringBuilder.append("\n");
			}

			System.out.println("Done");
		} catch (JWNLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
