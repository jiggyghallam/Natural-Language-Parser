package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

	public static void main(String[] args) {

		try {
			FileInputStream fis = new FileInputStream(
					"models/englishPCFG.ser.gz");
			ObjectInputStream englishPCFGModel = new ObjectInputStream(fis);
			edu.stanford.nlp.parser.lexparser.LexicalizedParser
					.loadModel(englishPCFGModel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
