package de.habibhaidari.foodcart.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static de.habibhaidari.foodcart.constant.FormatConstants.TIME_FORMAT;
import static de.habibhaidari.foodcart.constant.FormatConstants.WEEKDAYS_NAMES;

public class OpeningHour {

    private static final String EMPTY_STRING = "";
    private Integer id;

    private Integer n;
    private Integer from;
    private Integer to;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("created_at")
    private Date createdAt;


    public String getFromFormatted() {
        if (getFrom() < 0) {
            return "";
        }
        return String.format(TIME_FORMAT, getFrom() / 60, getFrom() % 60);
    }

    public String getToFormatted() {
        if (getFrom() < 0) {
            return "";
        }
        return String.format(TIME_FORMAT, getTo() / 60, getTo() % 60);
    }

    public String getWeekdayFormatted() {
        if (getN() >= 0 && getN() < 7) {
            return WEEKDAYS_NAMES[getN()];
        }
        return EMPTY_STRING;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

}
