package NLP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sf.extjwnl.JWNL;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.dictionary.MorphologicalProcessor;

public class WordNet {

	// "dict/non_mapped/clean_file.xml"
	private Dictionary dictionary;

	public WordNet(String location) {
		try {
			dictionary = Dictionary.getInstance(new FileInputStream(location));


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

	@Deprecated
	public void getWord(String str) throws JWNLException {
		IndexWord word = dictionary.getIndexWord(POS.NOUN, "dog");
		PointerTargetTree hyponyms = PointerUtils.getHyponymTree(word
				.getSenses().get(0));
		System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
		hyponyms.print();
	}

	public void printHyponymAllPOS(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				PointerTargetTree hyponyms = PointerUtils.getHyponymTree(iw
						.getSenses().get(0));
				System.out
						.println("Hyponym tree of \"" + iw.getLemma() + "\":");
				hyponyms.print();
			}
		}
	}

	public ArrayList<PointerTargetTree> getHyponymAllPOS(String word)
			throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		ArrayList<PointerTargetTree> hta = new ArrayList<PointerTargetTree>();
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				hta.add(PointerUtils.getHyponymTree(iw.getSenses().get(0)));
			}
		}
		return hta;
	}

	public void printHyponym(String word, POS pos) throws JWNLException {
		IndexWord iw = getStringAsIndexWord(word, pos);
		if (iw != null) {
			PointerTargetTree hyponyms = PointerUtils.getHyponymTree(iw
					.getSenses().get(0));
			System.out.println("Hyponym tree of \"" + iw.getLemma() + "\":");
			hyponyms.print();
		}
	}

	public PointerTargetTree getHyponymTree(String word, POS pos, int depth)
			throws JWNLException {
		IndexWord iw = getStringAsIndexWord(word, pos);
		if (iw != null) {
			return PointerUtils.getHyponymTree(iw.getSenses().get(0),depth);
		}
		return null;
	}
	
	public PointerTargetTree getHyponymTree(IndexWord word, int depth, int sense) {
		return PointerUtils.getHyponymTree(word.getSenses().get(sense),depth);
	}

	public void printDirectHypernym(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				PointerTargetNodeList hypernyms = PointerUtils
						.getDirectHypernyms(iw.getSenses().get(0));
				System.out.println("Direct hypernyms of \"" + iw.getLemma()
						+ "\":");
				hypernyms.print();
			}
		}
	}

	public void printHypernymTreeAllPOS(String word, int depth)
			throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				PointerTargetTree hypernymsTree = PointerUtils.getHypernymTree(
						iw.getSenses().get(0), depth);
				System.out.println("Hypernym tree of \"" + iw.getLemma()
						+ "\":");
				hypernymsTree.print();
			}
		}
	}

	public ArrayList<PointerTargetTree> getHypernymTreeAllPOS(String word,
			int depth) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		ArrayList<PointerTargetTree> hypernymsTreeArray = new ArrayList<PointerTargetTree>();
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				hypernymsTreeArray.add(PointerUtils.getHypernymTree(iw
						.getSenses().get(0), depth));

			}
		}
		return hypernymsTreeArray;
	}

	public void printHypernymTree(String word, int depth, POS pos)
			throws JWNLException {
		IndexWord indexWord = getStringAsIndexWord(word, pos);
		if (indexWord != null) {
			PointerTargetTree hypernymsTree = PointerUtils.getHypernymTree(
					indexWord.getSenses().get(0), depth);
			System.out.println("Hypernym tree of the " + pos.toString() + " \""
					+ indexWord.getLemma() + "\":");
			hypernymsTree.print();
		}
	}

	public PointerTargetTree getHypernymTree(String word, int depth, POS pos)
			throws JWNLException {
		IndexWord indexWord = getStringAsIndexWord(word, pos);
		if (indexWord != null) {
			PointerTargetTree hypernymsTree = PointerUtils.getHypernymTree(
					indexWord.getSenses().get(0), depth);
			return hypernymsTree;
		}
		return null;
	}
	
	public PointerTargetTree getHypernymTree(IndexWord word, int depth, int sense) {
		return PointerUtils.getHypernymTree(word.getSenses().get(sense), depth);
	}


	public void printSynonymsAllPOS(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				PointerTargetNodeList synonyms = PointerUtils.getSynonyms(iw
						.getSenses().get(0));
				System.out.println("Synonyms of \"" + iw.getLemma() + "\":");
				synonyms.print();
			}
		}
	}

	public void printSynonymTreeAllPOS(String word, int depth)
			throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				PointerTargetTree synonyms = PointerUtils.getSynonymTree(iw
						.getSenses().get(0), depth);
				System.out
						.println("Synonym tree of \"" + iw.getLemma() + "\":");
				synonyms.print();
			}
		}
	}

	public ArrayList<PointerTargetTree> getSynonymTreeAllPOS(String word,
			int depth) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		ArrayList<PointerTargetTree> sta = new ArrayList<PointerTargetTree>();
		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				sta.add(PointerUtils.getSynonymTree(iw.getSenses().get(0),
						depth));
			}
		}
		return sta;
	}

	public void printSynonymTree(String word, int depth, POS pos)
			throws JWNLException {
		IndexWord iw = getStringAsIndexWord(word, pos);
		if (iw != null) {
			PointerTargetTree synTree = PointerUtils.getSynonymTree(iw
					.getSenses().get(0), depth);
			System.out.println("Synonym tree of \"" + iw.getLemma() + "\":");
			synTree.print();

		}
	}

	public PointerTargetTree getSynonymTree(String word, int depth, POS pos)
			throws JWNLException {
		IndexWord iw = getStringAsIndexWord(word, pos);
		if (iw != null) {
			return PointerUtils.getSynonymTree(iw.getSenses().get(0).getSynset(), depth);
		}
		return null;
	}
	
	public PointerTargetTree getSynonymTree(IndexWord word, int depth, int sense) {
			return PointerUtils.getSynonymTree(word.getSenses().get(sense).getSynset(), depth);
	}


	public void printAsymmetricRelationship(String startstr, String endstr,
			int senses) throws JWNLException, CloneNotSupportedException {
		if (senses < 1) {
			System.err.println("Senses must be greater than 0");
			return;
		}

		ArrayList<IndexWord> indexWordArrayStart = getStringAsIndexWordArray(startstr);
		ArrayList<IndexWord> indexWordArrayEnd = getStringAsIndexWordArray(endstr);

		for (IndexWord start : indexWordArrayStart) {
			for (IndexWord end : indexWordArrayEnd) {
				if (start != null || end != null) {
					RelationshipList list = RelationshipFinder
							.findRelationships(start.getSenses().get(0), end
									.getSenses().get(0), PointerType.HYPERNYM);
					if (list.size() > 0) {
						System.out.println("Hypernym relationship between \""
								+ start.getLemma() + "\" and \""
								+ end.getLemma() + "\":");
						for (Object aList : list) {
							((Relationship) aList).getNodeList().print();
						}

						System.out.println("Common Parent Index: "
								+ ((AsymmetricRelationship) list.get(0))
										.getCommonParentIndex());
						System.out.println("Depth: " + list.get(0).getDepth());
					}
				}
			}
		}

	}

	public void printRelationship(String startstr, String endstr, int senses,
			PointerType type) throws JWNLException, CloneNotSupportedException {
		if (senses < 1) {
			System.err.println("Senses must be greater than 0");
			return;
		}

		ArrayList<IndexWord> indexWordArrayStart = getStringAsIndexWordArray(startstr);
		ArrayList<IndexWord> indexWordArrayEnd = getStringAsIndexWordArray(endstr);

		for (IndexWord start : indexWordArrayStart) {
			for (IndexWord end : indexWordArrayEnd) {
				if (start != null || end != null) {
					RelationshipList list = RelationshipFinder
							.findRelationships(start.getSenses().get(0), end
									.getSenses().get(0), type);
					if (list.size() > 0) {
						System.out.println(type.toString()
								+ " relationship between \"" + start.getLemma()
								+ "\" and \"" + end.getLemma() + "\":");
						for (Object aList : list) {
							((Relationship) aList).getNodeList().print();
						}

						switch (type) {
						case HYPERNYM:
							System.out.println("Common Parent Index: "
									+ ((AsymmetricRelationship) list.get(0))
											.getCommonParentIndex());
							System.out.println("Depth: "
									+ list.get(0).getDepth());
							break;
						case SIMILAR_TO:
							System.out.println("Depth: "
									+ list.get(0).getDepth());
							break;
						default:
							System.out.println("Depth: "
									+ list.get(0).getDepth());
							System.err
									.println("Type not in switch but tried my best");
							break;

						}
					}
				}
			}
		}

	}

	public void printEntailment(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);

		for (IndexWord iw : indexWordArray) {
			if (iw != null) {
				// RelationshipList list = RelationshipFinder.
			}
		}
	}

	public void printInfoAllPOS(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		for (IndexWord iw: indexWordArray) {
			System.out.println(iw.getSenses().get(0).getGloss());
		}
	}
	public ArrayList<String> getInfoAllPOS(String word) throws JWNLException {
		ArrayList<IndexWord> indexWordArray = getStringAsIndexWordArray(word);
		ArrayList<String> isa = new ArrayList<String>();
		for (IndexWord iw: indexWordArray) {
			isa.add(iw.getSenses().get(0).getGloss());
		}
		return isa;
	}

	public void printInfo(String word, POS pos) throws JWNLException {
		IndexWord indexWord = getStringAsIndexWord(word, pos);
		System.out.println(indexWord.getSenses().get(0).getGloss());
	}
	
	public String getInfo(String word, POS pos) throws JWNLException {
		IndexWord indexWord = getStringAsIndexWord(word, pos);
		if (indexWord == null)
			return "";
		return indexWord.getSenses().get(0).getGloss();

	}
	
	public String getInfo(IndexWord word, int sense) throws JWNLException {
		return word.getSenses().get(sense).getGloss();

	}

	public ArrayList<IndexWord> getStringAsIndexWordArray(String word)
			throws JWNLException {

		ArrayList<IndexWord> indexWordArray = new ArrayList<IndexWord>();
		IndexWord indexWord = null;
		boolean before = false;
		boolean after = false;
		for (POS pos : POS.values()) {
			if (indexWord == null)
				before = true;
			indexWord = getStringAsIndexWord(word, pos);
			if (indexWord != null)
				after = true;

			if (before && after) {
				indexWordArray.add(indexWord);
				before = false;
				after = false;
				indexWord = null;
			} else {
				before = false;
				after = false;
				indexWord = null;
			}
		}
		return indexWordArray;
	}

	public IndexWord getStringAsIndexWord(String word, POS pos) throws JWNLException {
		IndexWord indexWord = dictionary.getIndexWord(pos, word);
		if (indexWord == null) {
			MorphologicalProcessor morph =  dictionary.getMorphologicalProcessor();
			indexWord = morph.lookupBaseForm(pos, word);
			if (indexWord == null) {
				System.err.println("Word is NULL, not in dictionary");
			}
		}
		return indexWord;
	}

}
