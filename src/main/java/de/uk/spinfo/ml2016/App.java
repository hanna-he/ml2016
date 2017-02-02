package de.uk.spinfo.ml2016;

import de.uk.spinfo.ml2016.Components.ChiSquareCalculator;
import de.uk.spinfo.ml2016.Structures.Model;
import de.uk.spinfo.ml2016.Structures.ModelMaker;
import de.uk.spinfo.ml2016.io.JSONReader;
import wikiContext.Index;

public class App {
	public static void main(String[] args) {
		// falls das Index-File noch nicht existiert:
		// hier wird der Index für alle Ordner/Files in dem Ordner
		// "resources/extractedWiki" angelegt
		Index.makeIndexFile();

		//Erstellen den Models
		ModelMaker mm = new ModelMaker();
		Model model = mm.makeModel("Lemmas");
//		Model model2 = mm.makeModel("Stems");
//	    Model model3 = mm.makeModel("4-Grams");
		
		//x² berechnen mit 100 Schlüsselwörtern pro Klasse
		ChiSquareCalculator.computeChiSquare(model, 100);
		
		//Das Model in ein Json-File speichern
		// Writer.writeB(model);
		
		//Das Model wieder auslesen
//		JSONReader.readJSONFile("resources/json/MINI_Lemmas.json");
	}
}
