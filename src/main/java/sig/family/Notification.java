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
@Table(name="notifications")
@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	String message;
	Long memberId;
	int notificationType;

	@Column(columnDefinition="datetime")
	@JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	Date date;
	
	public Notification() {}

	public Notification(String message, Long memberId, int notificationType, Date date) {
		this.message = message;
		this.memberId = memberId;
		this.notificationType = notificationType;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Notification(Long id, String message, Long memberId, int notificationType, Date date) {
		this.id = id;
		this.message = message;
		this.memberId = memberId;
		this.notificationType = notificationType;
		this.date = date;
	}
	
}
