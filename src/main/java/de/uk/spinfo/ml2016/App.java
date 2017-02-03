package de.uk.spinfo.ml2016;

import de.uk.spinfo.ml2016.components.ChiSquareCalculator;
import de.uk.spinfo.ml2016.components.ModelMaker;
import de.uk.spinfo.ml2016.io.JSONReader;
import de.uk.spinfo.ml2016.io.Writer;
import de.uk.spinfo.ml2016.structures.BagOfWords;
import de.uk.spinfo.ml2016.structures.Model;
import de.uk.spinfo.ml2016.structures.Tool;
import de.uk.spinfo.ml2016.wikiContext.Index;

public class App {

	public static void main(String[] args) {
		// falls das Index-File noch nicht existiert:
		// hier wird der Index für alle Ordner/Files in dem Ordner
		// "resources/extractedWiki" angelegt
		Index.makeIndexFile();
		final long start = System.currentTimeMillis();
		//Erstellen des Models
		ModelMaker mm = new ModelMaker();
		Model model = mm.makeModel("Lemmas");
		final long middel1 = System.currentTimeMillis();
		Model model2 = mm.makeModel("Stems");
//	    Model model3 = mm.makeModel("4-Grams");
		
		final long middel2 = System.currentTimeMillis();
		//x² berechnen mit 100 Schlüsselwörtern pro Klasse
		ChiSquareCalculator.computeChiSquare(model2, 100);
		ChiSquareCalculator.computeChiSquare(model, 100);
		final long end = System.currentTimeMillis();
		//Das Model in ein Json-File speichern
//		 Writer.writeToJSON(model2);
		
		//Das Model wieder auslesen
//		Model model4 = JSONReader.readJSONFile("resources/json/MINI_Stems.json");

		System.out.println(end-start);
		System.out.println(middel1-start);
		System.out.println(middel2-middel1);
		System.out.println(middel2-start);
		System.out.println(end-middel2);
	}
}
