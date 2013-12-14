package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
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
	private static int MAX_SENSES = 5;

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
	private String[] conjecture = new String[4];
	private String[] hypothesis = new String[4];

	public KnowledgeBaseCreator() {
		f = new Formaliser();

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
	public boolean writeKnowledgeBaseToFile(String fileURL, boolean append)
			throws IOException {
		file = new File(fileURL);
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile(), append));
		System.out.println("Started writing to file");
		bw.append(stringBuilder.toString());
		bw.flush();
		bw.close();
		System.out.println("Finished writing to file");

		return true;
	}

	/**
	 * Processes the sentence part of a schema question
	 * 
	 * @param sentence
	 * @param hyponymDpeth
	 * @param hypernymDepth
	 * @param multipleSenses
	 *            allows the use of different senses of the word to be processed
	 * @return <code>true</code> if the sentence was processed
	 */
	public boolean processSentence(String sentence, int hyponymDpeth,
			int hypernymDepth, boolean multipleSenses) {
		System.out.println("Started Processing Sentence");
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
					if (multipleSenses) {
						for (int i = 0; i < w.getSenses() && i < MAX_SENSES; i++) {
							w.setHyponymTreeArray(wordNet.getHyponymTree(
									w.getIndexWord(), hyponymDpeth, i));
							w.setHypernymTreeArray(wordNet.getHypernymTree(
									w.getIndexWord(), hypernymDepth, i));
							w.setInfo(wordNet.getInfo(w.getIndexWord(), i));
							w.setSynonyms(wordNet.getSynonymTree(
									w.getIndexWord(), 5, i));
							addToKnowleadgeBase(w);
						}

					} else {
						w.setHyponymTreeArray(wordNet.getHyponymTree(
								w.getIndexWord(), hyponymDpeth, 0));
						w.setHypernymTreeArray(wordNet.getHypernymTree(
								w.getIndexWord(), hypernymDepth, 0));
						w.setInfo(wordNet.getInfo(w.getIndexWord(), 0));
						w.setSynonyms(wordNet.getSynonymTree(w.getIndexWord(),
								5, 0));
						addToKnowleadgeBase(w);
					}
				}

			}
			stringBuilder.append(f.getRuleAsString(root.trim(), dependencies,
					new String[] { "nsubj", "prep", "dobj", "iobj" }, false,
					false));

			System.out.println("Finished Processing Sentence");
		} catch (JWNLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void addToKnowleadgeBase(Word w) {
		w.compileWord(true);
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

	/**
	 * Processes the question
	 * 
	 * @param question
	 *            A string containing the question for example
	 *            <code>What is too small?.</code>
	 * @param answer
	 *            A string containing a single the answer to the question for
	 *            example <code>The suitcase.</code>
	 * @param multipleSenses
	 */
	public void processQuestion(String question, String answer,int hyponymDpeth,
			int hypernymDepth,boolean multipleSenses) {
		try {
			System.out.println("Started Processing Question");
			this.question = question;
			this.answer = answer;

			String specialString = question.split("\\?+")[1].trim()
					.toLowerCase();
			String answerString = answer.split("\\.+")[1].trim().toLowerCase();

			String taggedSentence = p.getPOSTaggedSentence(sentence);
			Word specialWord = p.getWord(specialString, taggedSentence, wordNet);

			
			if (!specialWord.isPronoun()) {
				if (multipleSenses) {
					for (int i = 0; i < specialWord.getSenses() && i < MAX_SENSES; i++) {
						specialWord.setHyponymTreeArray(wordNet.getHyponymTree(
								specialWord.getIndexWord(), hyponymDpeth, i));
						specialWord.setHypernymTreeArray(wordNet.getHypernymTree(
								specialWord.getIndexWord(), hypernymDepth, i));
						specialWord.setInfo(wordNet.getInfo(specialWord.getIndexWord(), i));
						specialWord.setSynonyms(wordNet.getSynonymTree(
								specialWord.getIndexWord(), 5, i));
						addToKnowleadgeBase(specialWord);
					}

				} else {
					specialWord.setHyponymTreeArray(wordNet.getHyponymTree(
							specialWord.getIndexWord(), hyponymDpeth, 0));
					specialWord.setHypernymTreeArray(wordNet.getHypernymTree(
							specialWord.getIndexWord(), hypernymDepth, 0));
					specialWord.setInfo(wordNet.getInfo(specialWord.getIndexWord(), 0));
					specialWord.setSynonyms(wordNet.getSynonymTree(specialWord.getIndexWord(),
							5, 0));
					addToKnowleadgeBase(specialWord);
				}
			}

			stringBuilder.append("fof(" + specialString + "_" + answerString
					+ ",conjecture," + specialString + "(" + answerString
					+ ")).");
			stringBuilder.append("\n");
			System.out.println("Finished Processing Question");
		} catch (JWNLException e) {
			System.err.println("Error when processing question");
			e.printStackTrace();

		}

	}
}
