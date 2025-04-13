package org.example.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersListModel {
    private boolean success;
    private List<OrderModel> orders;
    private int total;
    private int totalToday;
}