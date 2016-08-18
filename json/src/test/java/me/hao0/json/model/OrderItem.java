package me.hao0.json.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: haolin
 * Date:   8/18/16
 * Email:  haolin.h0@gmail.com
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 5022977319340054243L;

    private Long id;

    private Long buyerId;

    private Long sellerId;

    private Long orderId;

    private Long skuId;

    private String orderItemNo;

    private Integer quantity;

    private Integer status;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderItemNo() {
        return orderItemNo;
    }

    public void setOrderItemNo(String orderItemNo) {
        this.orderItemNo = orderItemNo;
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
        return "OrderItem{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", orderId=" + orderId +
                ", skuId=" + skuId +
                ", orderItemNo='" + orderItemNo + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
