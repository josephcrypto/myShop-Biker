package cn.coding.com.myshop;

import cn.coding.com.myshop.model.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SpringBootApplication
@RestController
public class MyShopApplication {

    public static void main(String[] args) {

        SpringApplication.run(MyShopApplication.class, args);
    }

    @PostMapping("/api/validatelogin")
    public String validatelogin(@RequestBody JSONObject jsonparam, HttpServletRequest request) {
        String username = jsonparam.get("username").toString();
        String password = jsonparam.get("password").toString();
        String str = "";

        Message message = new Message();
        if (username.equals("admin") && password.equals("admin666")) {
            HttpSession session = request.getSession();
            session.setAttribute("username", "admin");
            message.setCode(1);
            message.setMessage("Login Successfully");
            str = JSON.toJSONString(message);
        } else {
            message.setCode(0);
            message.setMessage("Invalid username or password!");
            str = JSON.toJSONString(message);
        }
        return str;
    }
}
