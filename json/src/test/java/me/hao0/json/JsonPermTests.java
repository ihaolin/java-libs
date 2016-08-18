package me.hao0.json;

import com.alibaba.fastjson.JSON;
import me.hao0.json.model.Order;
import me.hao0.json.model.OrderItem;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Author: haolin
 * Date:   8/18/16
 * Email:  haolin.h0@gmail.com
 */
public class JsonPermTests {

    private Order TEST_ORDER;

    private List<Order> TEST_ORDERS_10 = new ArrayList<>();

    private List<Order> TEST_ORDERS_100 = new ArrayList<>();

    private int TEST_ORDERS_TOTAL_COUNT = 100;

    private int TEST_ORDER_ITEM_TOTAL_COUNT = 5;

    private int LOOP_1000 = 1000;

    private int LOOP_10000 = LOOP_1000 * 10;

    private int LOOP_100000 = LOOP_10000 * 10;

    @Before
    public void setup(){

        Random orderItemCountRandom = new Random();

        TEST_ORDER = buildOrder(1L, generateOrderItemCount(orderItemCountRandom));

        TEST_ORDERS_10 = buildOrders(10, generateOrderItemCount(orderItemCountRandom));

        TEST_ORDERS_100 = buildOrders(100, generateOrderItemCount(orderItemCountRandom));

        System.out.println("TEST_ORDER byte size: " + JSON.toJSONString(TEST_ORDER).getBytes().length);

        System.out.println("TEST_ORDERS_10 byte size: " + JSON.toJSONString(TEST_ORDERS_10).getBytes().length);

        System.out.println("TEST_ORDERS_100 byte size: " + JSON.toJSONString(TEST_ORDERS_100).getBytes().length);
    }

    @Test
    public void testFastJsonSerializeByOrder(){

        // prepare hot
        JSON.toJSONString(TEST_ORDER);

        long start = System.currentTimeMillis();

        // loop 1000
        doFastJsonSerializeLoop(LOOP_1000, TEST_ORDER);
        System.out.println("loop " + LOOP_1000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 10000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_10000, TEST_ORDER);
        System.out.println("loop " + LOOP_10000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 100000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_100000, TEST_ORDER);
        System.out.println("loop " + LOOP_100000 + " times, cost: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testFastJsonSerializeByOrders10(){

        // prepare hot
        JSON.toJSONString(TEST_ORDERS_10);

        long start = System.currentTimeMillis();

        // loop 1000
        doFastJsonSerializeLoop(LOOP_1000, TEST_ORDERS_10);
        System.out.println("loop " + LOOP_1000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 10000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_10000, TEST_ORDERS_10);
        System.out.println("loop " + LOOP_10000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 100000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_100000, TEST_ORDERS_10);
        System.out.println("loop " + LOOP_100000 + " times, cost: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testFastJsonSerializeByOrders100(){

        // prepare hot
        JSON.toJSONString(TEST_ORDERS_100);

        long start = System.currentTimeMillis();

        // loop 1000
        doFastJsonSerializeLoop(LOOP_1000, TEST_ORDERS_100);
        System.out.println("loop " + LOOP_1000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 10000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_10000, TEST_ORDERS_100);
        System.out.println("loop " + LOOP_10000 + " times, cost: " + (System.currentTimeMillis() - start));

        // loop 100000
        start = System.currentTimeMillis();
        doFastJsonSerializeLoop(LOOP_100000, TEST_ORDERS_100);
        System.out.println("loop " + LOOP_100000 + " times, cost: " + (System.currentTimeMillis() - start));
    }

    private void doFastJsonSerializeLoop(int loopCount, Object obj) {
        for (int i=0; i<loopCount; i++){
            JSON.toJSONString(obj);
        }
    }

    private int generateOrderItemCount(Random orderItemCountRandom) {
        return orderItemCountRandom.nextInt(TEST_ORDER_ITEM_TOTAL_COUNT) + 1;
    }

    private List<Order> buildOrders(int orderCount, int orderItemRanCount){

        List<Order> orders = new ArrayList<>();

        for (int i = 1; i<= orderCount; i++){

            // order
            Order order = buildOrder((long)i, orderItemRanCount);

            orders.add(order);
        }

        return orders;
    }

    private Order buildOrder(Long orderId, int orderItemCount){
        // order
        Order order = new Order();
        order.setId(orderId);
        order.setBuyerId(1L);
        order.setSellerId(1L);
        order.setStatus(1);
        order.setOrderNo("T00000" + orderId);
        order.setDesc("订单" + order.getOrderNo());
        order.setCtime(new Date());
        order.setUtime(new Date());


        // order items
        List<OrderItem> items = new ArrayList<>();
        for (int j=1; j<=orderItemCount; j++){
            OrderItem orderItem = new OrderItem();
            orderItem.setId((long)j);
            orderItem.setOrderId(order.getId());
            orderItem.setBuyerId(order.getBuyerId());
            orderItem.setSellerId(order.getSellerId());
            orderItem.setStatus(1);
            orderItem.setOrderItemNo(order.getOrderNo() + j);
            orderItem.setQuantity(1);
            orderItem.setSkuId(4L);
            items.add(orderItem);
        }
        order.setItems(items);

        return order;
    }
}
