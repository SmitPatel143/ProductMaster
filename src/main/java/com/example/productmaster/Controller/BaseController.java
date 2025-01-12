package com.example.productmaster.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class BaseController {


    @GetMapping("/{fileName}")
    String getTemplate(@PathVariable String fileName) {
        return fileName;
    }
}
