package models.pojos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private final User author;
    private final Date date;
    private final String text;

    public Message(User author, Date date, String text) {
        this.author = author;
        this.date = date;
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (author != null ? !author.equals(message.author) : message.author != null) return false;
        if (date != null ? !date.equals(message.date) : message.date != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        return "[" + ft.format(date) + "] " + author.name + ": " + text;
    }
}
