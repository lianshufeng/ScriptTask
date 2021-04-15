package com.github.script.task.server.core.controller;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping()
public class PageController {

    /**
     *
     */
    private List<String> pages = null;


    @RequestMapping({"", "/", "index.html"})
    @SneakyThrows
    public void index(HttpServletResponse response) {
        response.sendRedirect("page/index.html");
    }

    @RequestMapping({"page", "page/"})
    @SneakyThrows
    public void page(HttpServletResponse response) {
        response.sendRedirect("index.html");
    }

    @RequestMapping("page/{viewName}.html")
    public Object page(@PathVariable("viewName") String viewName) {
        ModelAndView modelAndView = new ModelAndView("page/" + viewName);
        modelAndView.addObject("viewName", viewName);
        return modelAndView;
    }
}
