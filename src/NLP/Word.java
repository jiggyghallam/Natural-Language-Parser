package NLP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;

public class Word {

	private POS pos;
	private IndexWord indexWord;
	private String string;
	private PointerTargetTree synonymTree;
	private PointerTargetTree hypernymTree;
	private PointerTargetTree hyponymTree;
	private String info = "";
	private StringBuilder compiledInfomation;
	private int senses;
	
	private boolean name;

	public Word() {
	}

	public Word(IndexWord word, POS pos, int senses) {
		this.string = word.getLemma().toLowerCase();
		this.setIndexWord(word);
		if (pos == null) {
			name = true;
			pos = POS.NOUN;
		}
		this.setPos(pos);
		this.senses = senses;
		compiledInfomation = new StringBuilder();

	}
	
	public Word(String string, POS pos, int senses) {
		this.string = string.toLowerCase();
		this.setIndexWord(null);
		if (pos == null) {
			name = true;
			pos = POS.NOUN;
		}
		this.setPos(pos);
		this.senses = senses;
		compiledInfomation = new StringBuilder();

	}

	public Word(String string, POS pos, PointerTargetTree synonyms,
			PointerTargetTree hypernymTree, PointerTargetTree hyponymTree,
			String info) {
		this.string = string;
		this.setPos(pos);
		this.setSynonyms(synonyms);
		this.setHypernymTreeArray(hypernymTree);
		this.setHyponymTreeArray(hyponymTree);
		this.setInfo(info);
	}

	public String toString() {
		return string + "\n" + pos.toString() + "\n" + hypernymTree.toString()
				+ "\n" + hyponymTree.toString() + "\n" + info;
	}

	/**
	 * Compiles a word, so that all information is added in a list. Do not call this until all trees have been set.
	 * @param withComments
	 */
	public void compileWord(boolean withComments) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append("%Time Stamp: " + System.currentTimeMillis() + "\n");
		sb.append("%Human readable time: " + dateFormat.format(date) + " " + System.currentTimeMillis() + "\n");
		if (withComments) {
			sb.append("%Word: " + string + " is a " + pos.toString()
					+ " in sense number " + senses + " and pronoun is " + name
					+ ". The current meaning sense of " + string + " is: "
					+ info + ".\n\n");
		}

		switch (pos) {
		case NOUN:
			sb.append(nounCompile(withComments));
			break;
		case VERB:
			sb.append(verbCompile(withComments));
			break;
		case ADJECTIVE:
			sb.append(ajectiveCompile(withComments));
			break;
		case ADVERB:
			sb.append(adverbCompile(withComments));
			break;
		default:
			System.err.println("POS of word not found, unable to  compile");
		}

		compiledInfomation.append(sb);
		// System.out.println(sb.toString());
	}

	private String adverbCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	private String nameCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	private String nounCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		// Hypernym tree
		List<PointerTargetNodeList> hyperList = hypernymTree.toList();
		sb.append("\r\r\n%Hypernym tree of " + string + " in sense number " + senses + ". Meaning " + string
				+ " is a...\r\n");
		PointerTargetNode ptnCurrent = null;
		PointerTargetNode ptnPrevious = null;

		for (PointerTargetNodeList nl : hyperList) {
			nl = nl.reverse();
			ptnPrevious = null;
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				ptnCurrent = iterator.next();
				String[] hypernymsCurrentArr = getStringsFromPointerTargetNode(ptnCurrent);
				if (ptnPrevious != null) {
					String[] hypernymsPreviousArr = getStringsFromPointerTargetNode(ptnPrevious);
					for (String strPrev: hypernymsPreviousArr) {
						for (String strCurr: hypernymsCurrentArr) {
							String s = ("fof(" + strCurr + "_" + strPrev + ",axiom," + strCurr + "(X)=>" + strPrev + "(X)).\n");
							sb.append("fof(" + strCurr + "_" + strPrev + ",axiom," + strCurr + "(X)=>" + strPrev + "(X)).\n");
						}
					}
				} else {
					for (String str : hypernymsCurrentArr) {
						String s = ("fof(" + str + ",axiom," + str + "(X)).\n");
						sb.append("fof(" + str + ",axiom," + str + "(X)).\n");
					}
				}
				ptnPrevious = ptnCurrent;

			}

		}
		// Hyponym tree
		List<PointerTargetNodeList> hypoList = hyponymTree.toList();
		sb.append("\r\r\n%Hyponym tree of " + string + " in sense number " + senses + ". Meaning ... is a "
				+ string + "\n\n");
		for (PointerTargetNodeList nl : hypoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] hyponymsArr = getStringsFromPointerTargetNode(iterator
						.next());
				for (String str : hyponymsArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str	+ "=>" + string + ").\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str	+ "=>" + string + ").\n");
				}
			}
		}
		// Synonym tree
		List<PointerTargetNodeList> synoList = synonymTree.toList();
		sb.append("\r\r\n%Synonym tree of " + string + " in sense number " + senses + ".\n\n");
		for (PointerTargetNodeList nl : synoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] synoArr = getStringsFromPointerTargetNode(iterator
						.next());
				for (String str : synoArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str	+ "(X)<=>" + string + "(X)).\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str	+ "(X)<=>" + string + "(X)).\n");
				}
			}
		}
		return sb.toString();
	}

	private String pronounCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	private String verbCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		// Hypernym tree
		List<PointerTargetNodeList> hyperList = hypernymTree.toList();
		sb.append("\r\r\n%Hypernym tree of " + string + " in sense number " + senses + ". Meaning " + string
				+ " is a...\n\n");
		for (PointerTargetNodeList nl : hyperList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] hypernymsArr = getStringsFromPointerTargetNode(iterator.next());

				for (String str : hypernymsArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
				}

			}

		}
		// Hyponym tree
		List<PointerTargetNodeList> hypoList = hyponymTree.toList();
		sb.append("\r\r\n%Hyponym tree of " + string + " in sense number " + senses + ". Meaning ... is a "
				+ string + "\n\n");
		for (PointerTargetNodeList nl : hypoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] hypernymsArr = getStringsFromPointerTargetNode(iterator.next());

				for (String str : hypernymsArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
				}
			}
		}
		// Synonym tree
		List<PointerTargetNodeList> synoList = synonymTree.toList();
		sb.append("\r\r\n%Synonym tree of " + string + " in sense number " + senses + ".\n\n");
		for (PointerTargetNodeList nl : synoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] hypernymsArr =  getStringsFromPointerTargetNode(iterator.next());

				for (String str : hypernymsArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str
							+ "(" + string + ")).\n");
				}
			}
		}
		return sb.toString();
	}

	private String ajectiveCompile(boolean withComments) {
		StringBuilder sb = new StringBuilder();
		// Hypernym tree
		List<PointerTargetNodeList> hyperList = hypernymTree.toList();
		sb.append("\r\r\n%Hypernym tree of " + string + " in sense number " + senses + ". Meaning " + string
				+ " is a...\r\n");
		PointerTargetNode ptnCurrent = null;
		PointerTargetNode ptnPrevious = null;

		for (PointerTargetNodeList nl : hyperList) {
			nl = nl.reverse();
			ptnPrevious = null;
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				ptnCurrent = iterator.next();
				String[] hypernymsCurrentArr = getStringsFromPointerTargetNode(ptnCurrent);
				if (ptnPrevious != null) {
					String[] hypernymsPreviousArr = getStringsFromPointerTargetNode(ptnPrevious);
					for (String strPrev: hypernymsPreviousArr) {
						for (String strCurr: hypernymsCurrentArr) {
							String s = ("fof(" + strCurr + "_" + strPrev + ",axiom," + strCurr + "(X)=>" + strPrev + "(X)).\n");
							sb.append("fof(" + strCurr + "_" + strPrev + ",axiom," + strCurr + "(X)=>" + strPrev + "(X)).\n");
						}
					}
				} else {
					for (String str : hypernymsCurrentArr) {
						String s = ("fof(" + str + ",axiom," + str + "(X)).\n");
						sb.append("fof(" + str + ",axiom," + str + "(X)).\n");
					}
				}
				ptnPrevious = ptnCurrent;

			}

		}
		// Hyponym tree
		List<PointerTargetNodeList> hypoList = hyponymTree.toList();
		sb.append("\r\r\n%Hyponym tree of " + string + " in sense number " + senses + ". Meaning ... is a "
				+ string + "\n\n");
		for (PointerTargetNodeList nl : hypoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] hyponymsArr = getStringsFromPointerTargetNode(iterator
						.next());
				for (String str : hyponymsArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str	+ "=>" + string + ").\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str	+ "=>" + string + ").\n");
				}
			}
		}
		// Synonym tree
		List<PointerTargetNodeList> synoList = synonymTree.toList();
		sb.append("\r\r\n%Synonym tree of " + string + " in sense number " + senses + ".\n\n");
		for (PointerTargetNodeList nl : synoList) {
			for (Iterator<PointerTargetNode> iterator = nl.iterator(); iterator
					.hasNext();) {
				String[] synoArr = getStringsFromPointerTargetNode(iterator
						.next());
				for (String str : synoArr) {
					String s = ("fof(" + str + "_" + string + ",axiom," + str	+ "(X)<=>" + string + "(X)).\n");
					sb.append("fof(" + str + "_" + string + ",axiom," + str	+ "(X)<=>" + string + "(X)).\n");
				}
			}
		}
		return sb.toString();
	}

	private String[] getStringsFromPointerTargetNode(PointerTargetNode ptn) {

		String line = ptn.toString();
		String regex = "(\\[)(.*?)(Words: )(.*?)(\\s+--)(.*?)";
		Matcher m = Pattern.compile(regex).matcher(line);
		m.matches();
		String matches = m.group(4);
		String[] Arr = matches.split(",");
		ArrayList<String> temp = new ArrayList<String>();
		for (String str : Arr) {
			str = str.trim();
			str = str.replaceAll("\\W++", "_");
			str = str.toLowerCase();
			temp.add(str);
		}
		return temp.toArray(new String[0]);
	}

	public void print() {
		System.out.println(string);
		System.out.println("Pronoun = " + name);
		System.out.println(pos.toString());
		try {
			System.out.println("==== Hypernym Tree ====");
			hypernymTree.print();
			System.out.println("==== Hyponym Tree ====");
			hyponymTree.print();
			System.out.println("==== Synonym Tree ====");
			synonymTree.print();
		} catch (NullPointerException e) {
			System.out.println("No tree");
		}
		System.out.println(info);
	}

	public POS getPos() {
		return pos;
	}

	public void setPos(POS pos) {
		this.pos = pos;
	}

	public PointerTargetTree getSynonyms() {
		return synonymTree;
	}

	public void setSynonyms(PointerTargetTree synonyms) {
		this.synonymTree = synonyms;
	}

	public PointerTargetTree getHypernymTreeArray() {
		return hypernymTree;
	}

	public void setHypernymTreeArray(PointerTargetTree hypernymTreeArray) {
		this.hypernymTree = hypernymTreeArray;
	}

	public PointerTargetTree getHyponymTreeArray() {
		return hyponymTree;
	}

	public void setHyponymTreeArray(PointerTargetTree hyponymTreeArray) {
		this.hyponymTree = hyponymTreeArray;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public boolean isPronoun() {
		return name;
	}

	public void setPronoun(boolean pronoun) {
		name = pronoun;
	}

	public String getCompiledInfo() {
		return compiledInfomation.toString();
	}

	
	public int getSenses() {
		return senses;
	}

	public void setSenses(int sense) {
		this.senses = sense;
	}

	public IndexWord getIndexWord() {
		return indexWord;
	}

	public void setIndexWord(IndexWord indexWord) {
		this.indexWord = indexWord;
	}

}
