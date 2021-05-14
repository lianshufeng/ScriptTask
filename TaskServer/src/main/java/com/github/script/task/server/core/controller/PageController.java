package com.github.script.task.server.core.controller;

import com.github.script.task.server.core.service.SearchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping()
public class PageController {

    @Autowired
    private SearchService searchService;

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

    /**
     * 搜索引擎页面
     *
     * @return
     */
    @RequestMapping("page/search.html")
    public Object search() {
        ModelAndView modelAndView = new ModelAndView("page/search");
        modelAndView.addObject("viewName", "search");
        modelAndView.addObject("platforms", searchService.searchPlatforms());
        modelAndView.addObject("tags", searchService.searchTags());
        return modelAndView;
    }

}
