package ar.com.jb.countdownapp.entities;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity
public class Event {

    @Id
    private Long id;
    @Property
    private String title;
    @Property
    private String limit_date;
    @Property
    private String description;
    @Property
    private String color;
    @Property
    private String image;
    @Property
    private Boolean repeat;


    @Generated(hash = 1646674106)
    public Event(Long id, String title, String limit_date, String description,
            String color, String image, Boolean repeat) {
        this.id = id;
        this.title = title;
        this.limit_date = limit_date;
        this.description = description;
        this.color = color;
        this.image = image;
        this.repeat = repeat;
    }

    @Generated(hash = 344677835)
    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLimit_date() {
        return limit_date;
    }

    public void setLimit_date(String limit_date) {
        this.limit_date = limit_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }
}