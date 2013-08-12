package com.tistory.devyongsik.analyzer.dictionary;

public enum DictionaryType {
	NOUN("명사") {
		@Override
		public String getPropertiesKey() {
			return "noun.txt";
		}
	}
	,COMPOUND("복합명사") {
		@Override
		public String getPropertiesKey() {
			return "compounds.txt";
		}
	}
	,CUSTOM("사용자정의") {
		@Override
		public String getPropertiesKey() {
			return "custom.txt";
		}
	}
	,EOMI("어미-조사") {
		@Override
		public String getPropertiesKey() {
			return "eomi_josa.txt";
		}
	}
	,SYNONYM("동의어") {
		@Override
		public String getPropertiesKey() {
			return "synonym.txt";
		}
	}
	,STOP("불용어") {
		@Override
		public String getPropertiesKey() {
			return "stop.txt";
		}
	}
	;
	
	private String description;
	
	public String getDescription() {
		return description;
	}
	
	public abstract String getPropertiesKey();
	
	DictionaryType(String desc) {
		this.description = desc;
	}
}
