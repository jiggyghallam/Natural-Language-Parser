package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Dependency;
import common.Formaliser;

public class Dependency_Tests {

	@Test
	public void Test_isRelationshipAccepted() {
		Dependency d = new Dependency("nsubj(poured-2, I-1)");
		assertTrue(d.isRelationAccepted(new String[] {"nsubj", "prep","dobj"}));
		assertFalse(d.isRelationAccepted(new String[] {"prep","dobj"}));	
		d = new Dependency("prep_into(poured-2, I-1)");
		assertTrue(d.isRelationAccepted(new String[] {"nsubj", "prep","dobj"}));
		assertFalse(d.isRelationAccepted(new String[] {"nsubj","dobj"}));
	}

	
	@Test
	public void Test_isPersonalPronoun() {
		Dependency d1 = new Dependency("nsubj(poured-2, I-1)");
		Dependency d2 = new Dependency("nsubj(poured-2, he-1)");
		Dependency d3 = new Dependency("nsubj(poured-2, He-1)");
		Dependency d4 = new Dependency("nsubj(She-5, I-1)");
		assertTrue(d1.isNode2PersonalPronoun());
		assertTrue(d2.isNode2PersonalPronoun());
		assertTrue(d3.isNode2PersonalPronoun());
		assertTrue(d4.isNode1PersonalPronoun());
		assertFalse(d1.isNode1PersonalPronoun());
	}

}
