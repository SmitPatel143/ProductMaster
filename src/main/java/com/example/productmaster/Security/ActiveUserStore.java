package com.example.productmaster.Security;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActiveUserStore {
    private List<String> users;

    public ActiveUserStore() {
        users = new ArrayList<>();
    }

}
