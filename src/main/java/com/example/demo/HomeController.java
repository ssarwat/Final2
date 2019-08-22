package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    MessagesRepository messagesRepository;

    //For the home page path pointing to a list of all MESSAGES
    @RequestMapping("/")
    public String listMessages(Model model){
        model.addAttribute("messages", messagesRepository.findAll());
        return "list";
    }

    /*@RequestMapping("/")
    public String index(){
        return "list";
    }*/

    //For the add pat for adding messages
    @GetMapping("/add")
    public String addMessages(Model model){
        model.addAttribute("message", new Messages());
        return "messagepage";
    }

    @PostMapping("/process")
    public String processMessages(@Valid Messages message, BindingResult result){
        if(result.hasErrors()){
            return "messagepage";
        }
        messagesRepository.save(message);
        return "redirect:/";
    }


}
