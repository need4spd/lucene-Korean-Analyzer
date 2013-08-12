package com.tistory.devyongsik.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NounDictionaryDuplWordRemover {
	public static void main(String[] args) throws IOException {
		
		File nounFile = new File("/Users/need4spd/Programming/Java/workspace/walkingword/src/com/tistory/devyongsik/analyzer/dictionary/noun.txt");
		File customNounFile = new File("/Users/need4spd/Programming/Java/workspace/walkingword/src/com/tistory/devyongsik/analyzer/dictionary/custom.txt");
		
		
		InputStream nounIs = new FileInputStream(nounFile);
		InputStreamReader nounIsr = new InputStreamReader(nounIs);
		BufferedReader nounBr = new BufferedReader(nounIsr);
		
		Map<String, String> nounsMap = new HashMap<String, String>();
		
		String nounTemp = "";
		while((nounTemp = nounBr.readLine()) != null) {
			nounsMap.put(nounTemp, "");
		}
		
		InputStream customIs = new FileInputStream(customNounFile);
		InputStreamReader customIsr = new InputStreamReader(customIs);
		BufferedReader customBr = new BufferedReader(customIsr);
		
		Map<String, String> customMap = new HashMap<String, String>();
		
		String customTemp = "";
		while((customTemp = customBr.readLine()) != null) {
			customMap.put(customTemp, "");
		}
		
		int dupCount = 0;
		Set<String> customNounsKeySet = customMap.keySet();
		
		for(String customNoun : customNounsKeySet) {
			if (nounsMap.containsKey(customNoun)) {
				nounsMap.remove(customNoun);
				dupCount++;
			}
		}
		
		System.out.println("dup count : " + dupCount);
		
		customBr.close();
		customIsr.close();
		customIs.close();
		
		nounBr.close();
		nounIsr.close();
		nounIs.close();
		
		OutputStream nounOs = new FileOutputStream(nounFile, false);
		OutputStreamWriter osw = new OutputStreamWriter(nounOs);
		BufferedWriter bw = new BufferedWriter(osw);
		
		List<String> cleanedNouns = new ArrayList<String>();
		for(String n : nounsMap.keySet()) {
			cleanedNouns.add(n);
		}
		
		Collections.sort(cleanedNouns);
		
		for(String n : cleanedNouns) {
			bw.write(n);
			bw.write("\n");
		}
		
		bw.flush();
		bw.close();
		osw.close();
		nounOs.close();
		
	}
}
