package entities.gestionblog;

import java.sql.Date;

public class Commentaire {
    private int id_comment;
    private int user_id;
    private int id_post;
    private String content;
    private Date date;
    private int likes;

    public Commentaire(int id_comment, int user_id, int id_post, String content, Date date, int likes) {
        this.id_comment = id_comment;
        this.user_id = user_id;
        this.id_post = id_post;
        this.content = content;
        this.date = date;
        this.likes = likes;
    }

    public Commentaire() {
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id_comment=" + id_comment +
                ", user_id=" + user_id +
                ", id_post=" + id_post +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", likes=" + likes +
                '}';
    }
}
