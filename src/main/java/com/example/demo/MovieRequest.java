package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonProperty;

@RequestMapping
public class MovieRequest {
	List<Movie> movies;

	@JsonProperty("Search")
	public List<Movie> getMovies() {
		return movies;
	}

	@JsonProperty("Search")
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	
}
