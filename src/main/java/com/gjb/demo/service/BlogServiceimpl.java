package com.gjb.demo.service;

import com.gjb.demo.NotFoundException;
import com.gjb.demo.dao.BlogRepository;
import com.gjb.demo.model.Blog;
import com.gjb.demo.model.Type;
import com.gjb.demo.util.MarkdownUtils;
import com.gjb.demo.util.MyBeanUtils;
import com.gjb.demo.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceimpl implements BlogService {
    @Autowired
    private  BlogRepository blogRepository;

    //根据id查blog
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }

    //根据id查blog，并且把content（博客内容转化为html）
    @Transactional
    @Override
    public Blog getandConvert(Long id) {
        Blog blog=blogRepository.findOne(id);
        if(blog==null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=blog.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        //实现每次浏览，浏览次数加1
        blogRepository.updateViews(id);

        return b;
    }

    //按照封装的对象和pageable分页查询
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return  blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    //根据标签的id查询相应的blog
    @Override
    public Page<Blog> listBlog(Long tagid, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>(){
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join=root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagid);
            }
        },pageable);
    }

    //分页查询
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //根据是否包含字符串，查询并分页，得到blog的集合
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    //通过两个办法的调用，返回一个map集合，每个key是一个年份，value是blog集合
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepository.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }
    //保存blog
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    //更新博客
    @Transactional
    @Override
    public Blog updateBlog(Blog blog, Long id) {
        Blog b=blogRepository.findOne(id);
        if(b==null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    //删除博客
    @Transactional
    @Override
    public void deleteBlog(Long id) {
      blogRepository.delete(id);
    }

    //根据size的大小查询，前size个被推荐的blog
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort=new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable=new PageRequest(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    //查询blog的条数
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
