package my.eden.book.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by eden_liu on 2016/5/27.
 */
@Controller
@RequestMapping(value = {"", "/book"})
public class BookController {

    @RequestMapping(value = {"/","index"})
    public String index(Model model){
        model.addAttribute("index", "Hello Book!");
        return "book/index";
    }

}
