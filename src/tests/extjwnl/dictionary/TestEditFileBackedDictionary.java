package tests.extjwnl.dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Tests FileBackedDictionary editing.
 *
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class TestEditFileBackedDictionary extends DictionaryEditTester {

    protected static final String properties = "dict/non_mapped/clean_file.xml";

    @Override
    protected InputStream getProperties() throws FileNotFoundException {
        return new FileInputStream(properties);
    }
}
