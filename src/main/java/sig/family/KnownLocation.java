package sig.family;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="knownlocations")
@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnownLocation {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	double x,y;
	String name;
	boolean isSafe;
	
	public KnownLocation() {}
	
	public KnownLocation(double x, double y, String name, boolean isSafe) {
		this.x = x;
		this.y = y;
		this.name=name;
		this.isSafe=isSafe;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}
	
}
