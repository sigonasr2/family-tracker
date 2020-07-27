package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.websocket.server.PathParam;

@Service
@RestController
public class Controller {
	
	public static DataRepository database;
	RestTemplate connection = new RestTemplate();
	
	public Controller(DataRepository database) {
		this.database=database;
	}
	
	@GetMapping("/movies")
	public List<Movie> _8(@RequestParam("q") String q) {
		System.out.println(connection.getForObject("http://www.omdbapi.com/?apikey={key}&s={title}", String.class, "6ba5969a",q));
		MovieRequest mr = connection.getForObject("http://www.omdbapi.com/?apikey={key}&s={title}", MovieRequest.class, "6ba5969a",q);
		return mr.movies;
	}
	
	@GetMapping("")
	public Iterable<Data> _1(){
		return database.findAll();
	}
	@PostMapping("")
	public Data _2(@RequestBody Data data){
		return database.save(data);
	}
	@GetMapping("/data/{id}")
	public Data _3(@PathVariable Long id) {
		return database.findById(id).orElse(null);
	}
	@PutMapping("/data/{id}")
	public Object _5(@PathVariable Long id,@RequestBody Data data) {
		if (database.existsById(id)) {
			return database.save(data);
		} else {
			return "ID "+id+" does not exist!";
		}
	}
	@PatchMapping("/data/{id}")
	public Object _6(@PathVariable Long id,@RequestBody Data data) {
		if (database.existsById(id)) {
			return database.save(data);
		} else {
			return "ID "+id+" does not exist!";
		}
	}
	@GetMapping("/lessons/find/{name}")
	public Data _7(@PathVariable String name) {
		return database.findByName(name);
	}
	@GetMapping("/lessons/between")
	public List<Data> _8(@DateTimeFormat(pattern = "MM-dd-yyyy")@RequestParam("date1") Date date1,
			@DateTimeFormat(pattern = "MM-dd-yyyy")@RequestParam("date2") Date date2) {
		return database.submittedOnBetween(date1, date2);
	}
	@DeleteMapping("/data/{id}")
	public String _4(@PathVariable Long id) {
		if (database.existsById(id)) {
			database.deleteById(id);
			return "Removed ID "+id+" from database.";
		} else {
			return "ID "+id+" does not exist!";
		}
	}
	
	public static void downloadFileFromUrl(String url, String file) throws IOException{
		  File filer = new File(file);
		  filer.createNewFile();
		  
		  URL website = new URL(url);
		  HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		    /*for (String s : connection.getHeaderFields().keySet()) {
		    	System.out.println(s+": "+connection.getHeaderFields().get(s));
		    }*/
		    connection.setRequestMethod("GET");
		    //connection.setRequestProperty("Content-Type", "application/json");
		    try {
			  ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
			  FileOutputStream fos = new FileOutputStream(file);
			  fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			  fos.close();
		    } catch (ConnectException e) {
		    	System.out.println("Failed to connect, moving on...");
		    }
	  }
	
	public double CalculateArea(double width,double height) {
		return width*height;
	}
	
	public double CalculateArea(double radius) {
		return Math.PI*Math.pow(radius, 2);
	}

	@PostMapping("/math/area")
	public String areaDisplay(
			@RequestParam Map<String,String> map) {
		if (map.containsKey("type")) {
			if (map.get("type").equalsIgnoreCase("circle")) {
				if (map.containsKey("radius")) {
					return new StringBuilder("Area of a circle with a radius of ")
						.append(map.get("radius"))
						.append(" is "+CalculateArea(Double.parseDouble(map.get("radius"))))
						.toString();
				}
			}else if(map.get("type").equalsIgnoreCase("rectangle")) {
				if (map.containsKey("width") && map.containsKey("height")) {
					return new StringBuilder("Area of a ")
							.append(map.get("width")).append("x")
							.append(map.get("height"))
							.append(" rectangle is "+CalculateArea(Double.parseDouble(map.get("width")),Double.parseDouble(map.get("height"))))
							.toString();
				}
			}
		}
		return "Invalid";
	}
	
	@GetMapping("/math/volume/{l}/{w}/{h}")
	public String volumeDisplay(
			@PathVariable(value="l") String length,
			@PathVariable(value="w") String width,
			@PathVariable(value="h") String height) {
		return new StringBuilder("The volume of a ")
				.append(length).append("x")
				.append(width).append("x")
				.append(height)
				.append(" rectangle is ")
				.append(Integer.parseInt(length)*Integer.parseInt(width)*Integer.parseInt(height))
				.toString();
	}
	
	@GetMapping("/math/calculate")
	public String piDisplay(@RequestParam(value="operation",required=false) String operation,
			@RequestParam(value="x") String x,
			@RequestParam(value="y") String y) {
		int val1=Integer.parseInt(x);
		int val2=Integer.parseInt(y);
		switch (operation) {
			case "subtract":{
				return Integer.toString(val1-val2);
			}
			case "multiply":{
				return Integer.toString(val1*val2);
			}
			case "divide":{
				return Integer.toString(val1/val2);
			}
			default:{
				return Integer.toString(val1+val2);
			}
		}
	}
    
    @GetMapping("/time")
    public void updateTime(
        	@DateTimeFormat(pattern="MM-dd-yyyy HH:mm:ss")
        @RequestParam("startDate") Date date1,
    	@DateTimeFormat(pattern="MM-dd-yyyy HH:mm:ss")
        @RequestParam("endDate") Date date2
    ) {
        System.out.println(date1+"/"+date2);
    }
	
	@PostMapping("/math/sum")
	public String sumDisplay(@RequestParam Map<String,String> keys) {
		int sum = 0;
		for (String i : keys.keySet()) {
			sum += Integer.parseInt(keys.get(i));
		}
		return Integer.toString(sum);
	}
	
	@GetMapping("/math/pi")
	public String piDisplay() {
		return Double.toString(Math.PI);
	}

    @GetMapping("/image")
    public HashMap<String,String> helloWorld(@RequestParam("url") String url){
		try {
			downloadFileFromUrl("http://pbs.twimg.com/media/EdKE8xzVcCEf1qd.jpg","temp");
			BufferedImage img = ImageIO.read(new File("temp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}