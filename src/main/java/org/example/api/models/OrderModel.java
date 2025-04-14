package org.example.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private List<String> ingredients;
    private String id;
    private String status;
    private int number;
    private String createdAt;
    private String updatedAt;
}