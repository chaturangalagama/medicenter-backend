
package com.ilt.cms.report.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = {"home", "", "/"})
    public String showHomePage() {
        return "home";
    }

}
