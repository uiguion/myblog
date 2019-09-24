package com.gjb.demo.service;

import com.gjb.demo.model.Blog;
import com.gjb.demo.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    //得到
    Blog getBlog(Long id);
    //得到并把content转换成html
    Blog getandConvert(Long id);

    //分页（不用查询条件）
    Page<Blog> listBlog(Pageable pageable);
    //分页（根据封装对象的条件）
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    //分页（根据标签tag得id，去查相应的博客）
    Page<Blog> listBlog(Long tagid, Pageable pageable);
    //分页（按照是否包含字符串）
    Page<Blog> listBlog(String query,Pageable pageable);
    //查询每个年份的博客
    Map<String,List<Blog>> archiveBlog();
    //新增
    Blog saveBlog(Blog blog);
    //修改
    Blog updateBlog(Blog blog,Long id);
    //删除
    void deleteBlog(Long id);

    //查询推荐的前size条
    List<Blog> listRecommendBlogTop(Integer size);

    //统计blog的数目
    Long countBlog();
}
