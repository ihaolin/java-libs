package me.hao0.cassandra.model;

import java.io.Serializable;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 22/10/15
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 9198978251970786840L;

    private Long id;

    private Long userId;

    private String title;

    private Integer price;

    private Integer status;

    private Long ctime;

    private Long utime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
