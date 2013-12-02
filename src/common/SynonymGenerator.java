package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sf.extjwnl.JWNL;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.dictionary.Dictionary;

public class SynonymGenerator {

	// "dict/non_mapped/clean_file.xml"
	private Dictionary dictionary;

	public SynonymGenerator(String location) {
		try {
			dictionary = Dictionary.getInstance(new FileInputStream(location));

			// IndexWord word = wordnet.getIndexWord(POS.VERB, "run");
			// IndexWord s = wordnet.
			/*
			 * IndexWordSet set = wordnet.lookupAllIndexWords("run");
			 * System.out.println(set.getLemma().isEmpty()); String str =
			 * set.getLemma(); System.out.println(str); IndexWord word =
			 * set.getIndexWordArray(); System.out.println(word.length);
			 */
			// Turn it into an array of IndexWords
			/*
			 * IndexWord ws = set.getIndexWordArray(); POS p = ws.getPOS();
			 * Set<String> synonyms = new HashSet<String>(); //IndexWord
			 * indexWord = wordnet.lookupIndexWord(POS.NOUN, "wife"); IndexWord
			 * indexWord = wordnet.lookupIndexWord(p, "director"); List<Synset>
			 * synSets = indexWord.getSenses(); for (Synset synset : synSets) {
			 * List<Word> words = synset.getWords(); for (Word word : words) {
			 * synonyms.add(word.getLemma()); } } System.out.println(synonyms);
			 */

		} catch (FileNotFoundException e) {
			System.err
					.println(e.getMessage()
							+ "\n Basically the dictionary was not found, look at clean_file.xml and check the dictionary path.");
			// e.printStackTrace();
		} catch (JWNLException e) {
			// // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getWord(String str) throws JWNLException {
		IndexWord word = dictionary.getIndexWord(POS.NOUN, "dog");
        PointerTargetTree hyponyms = PointerUtils.getHyponymTree(word.getSenses().get(0));
        System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
        hyponyms.print();

	}

}
