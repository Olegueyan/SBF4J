package web.olegueyan.sbf4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(Model model)
    {
        ModelAndView modelMap = new ModelAndView("home");
        modelMap.addObject("test", "test");
        System.out.println("Calling controller");
        return modelMap;
    }
}