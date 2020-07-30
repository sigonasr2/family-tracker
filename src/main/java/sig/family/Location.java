package sig.family;

import java.lang.reflect.Field;
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
@Table(name="locations")
@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	double x,y;
	Long memberId;

	@Column(columnDefinition="datetime")
	@JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	Date date;
	
	public Location() {}
	
	public Location(double x, double y, Long memberId, Date date) {
		this.x = x;
		this.y = y;
		this.memberId = memberId;
		this.date=date;
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
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()+"(");
		boolean first=true;
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!first) {
				sb.append(",");
			}
			try {
				sb.append(f.getName()+"="+f.get(this));
				first=false;
			} catch (IllegalArgumentException|IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
