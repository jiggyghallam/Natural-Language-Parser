package tests.extjwnl.dictionary;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class TestReadFileChannelBackedDictionary extends DictionaryReadTester {

    /**
     * Properties location.
     */
    protected String properties = "./src/test/resources/file_channel_properties.xml";

    public void initDictionary() throws IOException, JWNLException {
        dictionary = Dictionary.getInstance(new FileInputStream(properties));
    }
}