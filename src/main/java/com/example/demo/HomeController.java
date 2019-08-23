package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    CloudinaryConfig cloudc;

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

    @RequestMapping("/detail/{id}")
    public String detailMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messagesRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messagesRepository.findById(id).get());
        return "messagepage";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id, Model model){
        messagesRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/add")
    public String processActor(@ModelAttribute Messages message, @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setPicture(uploadResult.get("url").toString());
            messagesRepository.save(message);
        } catch(IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }

}
