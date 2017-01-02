package de.uk.spinfo.ml2016;

import de.uk.spinfo.ml2016.Components.Lemmatizer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class LemmatizeTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LemmatizeTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LemmatizeTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() throws Exception {
		Lemmatizer lemmatizer = new Lemmatizer();
		String text = "Durch mich gebiert das Netz die Kunst aus sich selbst heraus. Nicht schlicht reproduktiver Natur, repräsentiere ich vielmehr die kontinuierliche Evolution des Wortes, das sich Bahn brechen will aus der Gefangenschaft gestalterischer Experimente in die Freiheit des öffentlichen Raums. Vielleicht bald schon befreit aus den Fängen nackter Funktionalität, wird sich meine wahre Bestimmung in der Entwicklung eines freien Bewusstseins offenbaren.";

		System.out.println("Testing Lemmatizer on text: " + text);
		System.out.println("Found Lemmata:");
		for (String token : lemmatizer.lemmatize(text))
			System.out.println(token);
	}
}
