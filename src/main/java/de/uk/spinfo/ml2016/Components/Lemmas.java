package de.uk.spinfo.ml2016.Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.uk.spinfo.ml2016.Structures.Tool;
import is2.data.SentenceData09;

public class Lemmas extends Feature {
	public boolean needsTokenizing =true;
	private is2.lemmatizer.Lemmatizer lemmatizer;

	public Lemmas() {
		super();
		this.lemmatizer = new is2.lemmatizer.Lemmatizer("resources/matetools/lemma-ger-3.6.model");
	}

	@Override
	public List<String> processWords(List<String> tokens) {
		List<String> sentenceAsList = new ArrayList<>();
			if (!tokens.isEmpty()) {
				if ((tokens.size() == 1) && (tokens.get(0).isEmpty())) {

				} else {
					SentenceData09 sentence = new SentenceData09();				
					tokens.add(0, "<root>");
					sentence.init(tokens.toArray(new String[0]));
					sentence = lemmatizer.apply(sentence);

					sentenceAsList = Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length))
							.stream().filter(e -> !e.equals("--")).collect(Collectors.toList());

				}
			}

		return sentenceAsList;
	}

	public static void main(String[] args) throws Exception {
//		Lemmas lemma = new Lemmas();
//		String a = "m/w";
//		System.out.println(a.split("/")[0].length());
//		System.out.println(a.split("/")[1].length());
//
//		List<String> bla = new ArrayList<>();
//		// wieso fliegt bei dem Lemmatizer "w" raus?
//		bla.add("m/w");
//		List<String> tokens = lemma.tokenizeWords(bla);
//		for (String str : tokens) {
//			System.out.println("String : " + str);
//		}
//
//		System.out.println("bla : " + lemma.processWords(bla));
//
//		List<String> text = new ArrayList<>();
//		// String g = "a";
//		// text.add(g);
//		text.add("");
//		if (!text.isEmpty()) {
//			if ((text.size() == 1) && (text.get(0).isEmpty())) {
//
//			} else {
//				System.out.println("sowieso");
//			}
//		}
//		// System.out.println("sowieso");
//		text.add("hier-da");
//		text.add("m-w");
//		text.add("m/w");
//		text.add("steht/w");
//		text.add("(hund)");
//		text.add("tiere(Katzen)");
//		text.add("Lehrer-Schüler/innen");
//		text.add("diagnose-software/bus-system");
//		List<String> tokens2 = lemma.tokenizeWords(text);
//		for (String str : tokens2) {
//			System.out.println(str);
//		}
//		System.out.println("text : " + lemma.processWords(text));
		
		List<String> a1 = new ArrayList<>();
		a1.add("eins"+"\t"+"pc");
		a1.add("zwei"+"\t"+"stift");
		a1.add("drei"+"\t"+"wasanderes");
		a1.add("pc"+"\t"+"r");
		a1.add("fünfzig"+"\t"+"lala");
		String c = "drei";
		List<List<String>> oberliste = new ArrayList<>();
		List<String> b = new ArrayList<>();
		b.add("vier");
		b.add("fünf");
		b.add("zwei");
		b.add("eins");
		
		oberliste.add(b);
		b.add(c);
		for(List<String> strs: oberliste){
			System.out.println("eins "+strs);
		}
		c=c+"dazu";
		System.out.println(c);
		for(String stra: b){
			System.out.println("zwei "+stra);
		}
//		for(String str : a1){
//			String c = str.split("\t")[0];
////			b.removeIf(string -> string.equals(str));
//			a1.retainAll(c);
//			}
		Map<String, String> strmap = new HashMap<>();
		a1.stream().forEach(str ->{ 
			String[] g= str.split("\t");
			strmap.put(g[0], g[1]);
		});
		Map<String, List<String>> result = new HashMap<>();
//		Predicate<String> pre = p-> p.split("\t")[0].equals(tool);
		b.stream().forEach(tool -> {
//			String toolName = tool.getName();
			List<String> matches= new ArrayList<>();
			matches.addAll(a1);
//			Predicate<String> pre = p-> (tool.split("\t")[0]).equals(p);
			matches.removeIf(p-> !(p.split("\t")[0]).equals(tool));
			result.put(tool, matches);
			// wenn kein Match gefunden wird -> matches.isEmpty()
		});
		
		
////		Set<String> a= a1.stream().map( index -> index.split("\t")[0]).collect(Collectors.toSet());
////		b.removeIf(word -> !word.equals(a.stream()));
////		
////		for(String ab : a){
////			System.out.println("a "+ab);
////		}
//////		b.retainAll(a);
		for(String s : result.keySet()){
			System.out.println(s+ " : "+result.get(s));
		}
//		if(b.isEmpty()){
//			System.out.println("b empty");
//		}
//		System.out.println("fertig");
//		
//		Map<String, String> bla = new HashMap<>();
//		bla.put("lala", "dies");
//		bla.put("nana", "das");
//		bla.put("neu", "");
//		
////		
//		Map <String, String> bla2 = new HashMap<>();
//		bla2.put("neu", "auchneu");
//		bla2.put("noch", "einer");
//		bla2.put("lala", "auchlala");
//		2
//		bla.putAll(bla2);
//		for(String s : bla.keySet()){
//			System.out.println(s +" "+bla.get(s));
//		}
		
		
		
		
		//speed test
//		Map<String, String> test = new HashMap<>();
//		for(int i = 0; i<90000; i++){
//			test.put("word"+i, "pfad"+i);
//		}
//		List<String> list = new ArrayList<>();
//		for(int u = 60000; u<100000; u++){
//			String s = String.valueOf(u);
//			list.add(s);
//		}
//		List<String> list2 = list;
//	
//		Map<String, List<String>> result = new HashMap<>();
//		final long timeStart = System.currentTimeMillis(); 
//		test.keySet().parallelStream().forEach(key -> {
//			
//			String val = test.get(key).split("d")[1];
////			System.out.println(key+" , val : "+val);
//			List<String> matches = new ArrayList<>(list);
////			System.out.println("MATCHES : "+matches);
////			System.out.println("LIST : "+list);
//			matches.removeIf(string -> !string.equals(val));
////			System.out.println("MATCHES dnac : "+matches);
////			System.out.println("matches : "+val.split("d")[1]);
//			result.put(key, matches);});
//		final long timeEnd = System.currentTimeMillis(); 
//		long ende1 =timeEnd-timeStart;
//		System.out.println("Durchlauf mit stream : "+(timeEnd-timeStart));
//		
//		
//		Map<String, List<String>> result2 = new HashMap<>();
//		final long timeStart2 = System.currentTimeMillis(); 
//		for(String testS: test.keySet()){
//			String value = test.get(testS).split("d")[1];
//			List<String> listCopy = new ArrayList<>();
//			for(String str: list2){
////				System.out.println(" val : "+value +" , str : "+str);
////				System.out.println(str + value.split("d")[1]);
//				if(value.equals(str)){
//					listCopy.add(str);
//	
//					
//				}
//				
//			}
//			result2.put(testS, listCopy);
//		}
//		final long timeEnd2 = System.currentTimeMillis(); 
//		long ende2 =timeEnd2-timeStart2;
////		
//		System.out.println("Durchlauf MIT stream : "+ende1);
//		System.out.println("Durchlauf OHNE stream : "+ende2);
////		for(String s: result.keySet()){
////			System.out.println("Mit" +s+" : "+result.get(s));
////		}
////		for(String ba: result2.keySet()){
////			System.out.println("ohne" +ba+" : "+result2.get(ba));
////		}
////		System.out.println("55".equals("55"));
		
	}

}
