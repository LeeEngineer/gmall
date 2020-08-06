package com.atguigu.gmall.index.controller;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-30 19:58
 */
@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping
    public String toIndex(Model model){
        List<CategoryEntity> lev1Category = null;
        try {
            lev1Category = indexService.getLev1Category();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        model.addAttribute("categories", lev1Category);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/cates/{pid}")
    public ResponseVo<List<CategoryEntity>> getSubCategories(@PathVariable Long pid){
        List<CategoryEntity> categoryEntities = null;
        try {
            categoryEntities = indexService.getSubCategories(pid);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseVo.ok(categoryEntities);

    }

}
