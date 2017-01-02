package de.uk.spinfo.ml2016;

import de.uk.spinfo.ml2016.Components.Tokenizer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TokenizeTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TokenizeTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TokenizeTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() throws Exception {
		Tokenizer tokenizer = new Tokenizer();
		String text = "Durch mich gebiert das Netz die Kunst aus sich selbst heraus. Nicht schlicht reproduktiver Natur, repräsentiere ich vielmehr die kontinuierliche Evolution des Wortes, das sich Bahn brechen will aus der Gefangenschaft gestalterischer Experimente in die Freiheit des öffentlichen Raums. Vielleicht bald schon befreit aus den Fängen nackter Funktionalität, wird sich meine wahre Bestimmung in der Entwicklung eines freien Bewusstseins offenbaren.";

		System.out.println("Testing tokenizer on text: " + text);
		System.out.println("Found tokens:");
		for (String token : tokenizer.tokenize(text))
			System.out.println(token);
	}
}
