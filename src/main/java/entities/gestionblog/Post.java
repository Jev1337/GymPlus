package entities.gestionblog;

import java.sql.Date;

public class Post {

    private int id_post;
    private int user_id;
    private String mode;
    private String content;
    private Date date;
    private String photo;
    private int likes;

    public Post( int user_id, String mode, String content, Date date, String photo, int likes) {
        //this.id_post = id_post;
        this.user_id = user_id;
        this.mode = mode;
        this.content = content;
        this.date = date;
        this.photo = photo;
        this.likes = likes;
    }

    public Post() {
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id_post=" + id_post +
                ", user_id=" + user_id +
                ", mode='" + mode + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", photo='" + photo + '\'' +
                ", likes=" + likes +
                '}';
    }
}
