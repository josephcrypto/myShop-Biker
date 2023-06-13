package cn.coding.com.myshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        model.addAttribute("username", username);
        return "home";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
