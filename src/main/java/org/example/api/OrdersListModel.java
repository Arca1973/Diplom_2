package org.example.api;

import java.util.List;

public class OrdersListModel {
    private boolean success;
    private List<OrderModel> orders;
    private int total;
    private int totalToday;
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }

    public OrdersListModel(boolean success, List<OrderModel> orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "success=" + success +
                ", orders=" + orders +
                ", total=" + total +
                ", totalToday=" + totalToday +
                '}';
    }
}
