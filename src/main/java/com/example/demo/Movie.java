package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
	String title;
	String imdbId;
	String poster;
	int year;

	@JsonCreator
	Movie(
			@JsonProperty("Title") String title,
			@JsonProperty("imdbID") String imdbId,
			@JsonProperty("Poster") String poster,
			@JsonProperty("Year") int year) {
		this.title=title;
		this.imdbId=imdbId;
		this.poster=poster;
		this.year=year;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}
	@JsonProperty("imdbId")
	public String getImdbId() {
		return imdbId;
	}
	@JsonProperty("imdbId")
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	@JsonProperty("poster")
	public String getPoster() {
		return poster;
	}
	@JsonProperty("poster")
	public void setPoster(String poster) {
		this.poster = poster;
	}
	@JsonProperty("year")
	public int getYear() {
		return year;
	}
	@JsonProperty("year")
	public void setYear(int year) {
		this.year = year;
	}
	
	
}
