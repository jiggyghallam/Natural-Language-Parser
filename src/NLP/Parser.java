package NLP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import simplenlg.features.Tense;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class Parser {

	private String fileURL;
	private String taggerURL;
	private LexicalizedParser lp;
	private MaxentTagger tagger;
	private TenseModifier tenseModifier;

	public Parser(String fileURL, String taggerURL, String[] options) {
		this.fileURL = fileURL;
		this.taggerURL = taggerURL;
		lp = LexicalizedParser.loadModel(fileURL);
		tagger = new MaxentTagger(taggerURL);
		lp.setOptionFlags(options);
		tenseModifier = new TenseModifier();
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

	/**
	 * Returns only a single sentence, however can be modified to return
	 * multiple.
	 * 
	 * @param s
	 * @return
	 */
	public String getPOSTaggedSentence(String s) {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(s.getBytes())));
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(br);

		for (List<HasWord> sentence : sentences) {
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			return Sentence.listToString(tSentence, false);
		}
		return null;
	}

	/**
	 * Creates a {@link Word} from a {@link Dependency} and a tagged sentence containing that word.
	 * @param dependency
	 * @param POSTaggedString
	 * @param wordNet
	 * @return
	 */
	public Word getWord(Dependency dependency, String POSTaggedString, WordNet wordNet) {
		String wordStr = dependency.getNode_2_string();
		String[] POSTaggedStringArr = POSTaggedString.split("\\s");
		// System.out.println(POSTaggedString.length());
		String tag = "";
		for (String s : POSTaggedStringArr) {
			if (s.contains(wordStr)) {
				tag = s;
				break;
			}
		}
		tag = tag.split("\\W")[1];
		try {
			IndexWord iw = wordNet.getStringAsIndexWord(tenseModifier.changeTense(wordStr, Tense.PRESENT), getPOSfromTag(tag));
			if (iw == null) {
				System.err.println("A name has been found, replaced the name " + wordStr + " with human");
				iw = wordNet.getStringAsIndexWord("human", POS.NOUN);
				return new Word(iw, POS.NOUN,iw.getSenses().size());
			}
			
			return new Word(iw, getPOSfromTag(tag),iw.getSenses().size());
		} catch (JWNLException e) {
			System.err.println("Exception thrown when creating a word in parser\n");
			e.printStackTrace();
			return new Word(wordStr, POS.NOUN,1);
		}
	}
	
	/**
	 * Creates a {@link Word} from a String and a tagged sentence containing that word.
	 * @param word
	 * @param POSTaggedString
	 * @param wordNet
	 * @return
	 */
	public Word getWord(String word, String POSTaggedString, WordNet wordNet) {
		String wordStr = word;
		String[] POSTaggedStringArr = POSTaggedString.split("\\s");
		// System.out.println(POSTaggedString.length());
		String tag = "";
		for (String s : POSTaggedStringArr) {
			if (s.contains(wordStr)) {
				tag = s;
				break;
			}
		}
		tag = tag.split("\\W")[1];
		try {
			IndexWord iw = wordNet.getStringAsIndexWord(tenseModifier.changeTense(wordStr, Tense.PRESENT), getPOSfromTag(tag));
			if (iw == null) {
				System.err.println("A name has been found, replaced the name " + wordStr + " with human");
				iw = wordNet.getStringAsIndexWord("human", POS.NOUN);
				return new Word(iw, POS.NOUN,iw.getSenses().size());
			}
			
			return new Word(iw, getPOSfromTag(tag),iw.getSenses().size());
		} catch (JWNLException e) {
			System.err.println("Exception thrown when creating a word in parser\n");
			e.printStackTrace();
			return new Word(wordStr, POS.NOUN,1);
		}
	}

	private POS getPOSfromTag(String tag) {
		if (tag.startsWith("JJ"))
			return POS.ADJECTIVE;
		if (tag.startsWith("NN"))
			return POS.NOUN;
		if (tag.startsWith("RB"))
			return POS.ADVERB;
		if (tag.startsWith("VB"))
			return POS.VERB;
		return POS.NOUN;
	}

}
