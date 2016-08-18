package me.hao0.json.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author: haolin
 * Date:   8/18/16
 * Email:  haolin.h0@gmail.com
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 6713121366614075299L;

    private Long id;

    private Long buyerId;

    private Long sellerId;

    private String orderNo;

    private Integer status;

    private String desc;

    private List<OrderItem> items;

    private Date ctime;

    private Date utime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", orderNo='" + orderNo + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", items=" + items +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
