package tests.extjwnl.data;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.AdjectiveSynset;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public class TestAdjSynset extends BaseData {

    @Test
    public void setIsAdjectiveCluster() throws JWNLException {
        AdjectiveSynset synset = new AdjectiveSynset(dictionary);
        synset.setIsAdjectiveCluster(true);
        Assert.assertTrue(synset.isAdjectiveCluster());
    }
}
