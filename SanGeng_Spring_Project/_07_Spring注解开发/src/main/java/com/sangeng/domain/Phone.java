package com.sangeng.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component("phone")
public class Phone {
    private double price;
    private String name;
    private String password;
    private String path;

}
