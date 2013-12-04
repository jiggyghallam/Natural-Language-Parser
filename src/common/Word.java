package common;


import java.util.ArrayList;

import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.list.PointerTargetTree;

public class Word {
	
	private POS pos;
	private String string;
	private ArrayList<String> synonyms;
	private PointerTargetTree hypernymTreeArray;
	private PointerTargetTree hyponymTreeArray;
	private String info;
	
	public Word(){}
	
	public Word(String string, POS pos, ArrayList<String> synonyms, PointerTargetTree hypernymTree, PointerTargetTree hyponymTree, String info) {
		this.string = string;
		this.pos = pos;
		this.synonyms = synonyms;
		this.hypernymTreeArray = hypernymTree;
		
		this.info = info;
	}
	
	public String toString() {
		return string;
	}

}
