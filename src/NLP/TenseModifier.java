package NLP;

import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.*;
import simplenlg.realiser.english.Realiser;
public class TenseModifier {
	
	SPhraseSpec p;
	Lexicon lexicon;
    NLGFactory nlgFactory;
    Realiser realiser;
	public TenseModifier () {
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		
	}
	
	public String changeTense(String verb, Tense tense) {
		
		WordElement word = lexicon.getWordFromVariant(verb);
		InflectedWordElement infl = new InflectedWordElement(word);
		infl.setFeature(Feature.TENSE, tense);
		Realiser realiser = new Realiser(lexicon);
		String present = realiser.realise(infl).getRealisation();
		return present;

		
	}

}
