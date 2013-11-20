package common;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

/**
 * Processes Strings into Tree's used by the stanford parser
 * @author George
 *
 */
public class SentanceProcessor {
	LexicalizedParser lp;
	TokenizerFactory<CoreLabel> tokenizerFactory;
	
	public SentanceProcessor(String fileURL, String[] options) {
		lp = LexicalizedParser.loadModel(fileURL);
		lp.setOptionFlags(options);
		tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	}
    
    

public Tree processSentence(String sentence) {
    List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();
    Tree bestParse = lp.parseTree(rawWords);
    return bestParse;
}

}
