package ch.hearc.malabar_jokes.jokes.model;

//import javax.persistence.Entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Joke {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public String title;
    public String content;
    public String date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Joke() {
    }

    public Joke(String title, String content, String date, User user) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return user.getUserName();
    }

    public int getUserId() {
        return user.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
