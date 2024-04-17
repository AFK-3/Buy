package id.ac.ui.cs.advprog.buy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class BuyController {
    @GetMapping("/")
    @ResponseBody
    public String buyPage(){
        return "<h1>Hello World!<h1>";
    }
}
