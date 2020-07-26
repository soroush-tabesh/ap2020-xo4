package ir.soroushtabesh.xo4.server.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer user_id;
    private String username;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String event;
    private String description;
    @Enumerated(EnumType.STRING)
    private Severity severity;

    public Log() {
    }

    public Log(int user_id, Date date, String event, String description, Severity severity) {
        this.user_id = user_id;
        this.date = date;
        this.event = event;
        this.description = description;
        this.severity = severity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return getId().equals(log.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return "Log{" +
                "log_id=" + id +
                ", user_id=" + user_id +
                ", username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", event='" + event + '\'' +
                ", description='" + description + '\'' +
                ", severity=" + severity +
                '}';
    }

    public enum Severity {
        INFO, WARNING, FATAL
    }
}
