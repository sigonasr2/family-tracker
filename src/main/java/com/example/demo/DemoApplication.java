package com.example.demo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		Date date1 = new Date();
		Date date2 = (Date)date1.clone();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date2);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
		System.out.println();
		
	}

}
