package com.example.webt.form;

import lombok.Data;

@Data
public class WebtQuery {
	
	private String title;
	private String author;
	private String[] genres;
	private String startYearFrom;
	private String startYearTo;
	private String done;

	public WebtQuery() {
		title = "";
		author = "";
		genres = new String[]{};
		startYearFrom = "";
		startYearTo = "";
		done = "";
	}
}