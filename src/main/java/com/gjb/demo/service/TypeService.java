package com.gjb.demo.service;

import com.gjb.demo.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    //存
    Type saveType(Type type);
    //得
    Type getType(Long id);
    //分页
    Page<Type> listType(Pageable pageable);

    //更新
    Type updateType(Long id,Type type);

    //删除
    void deleteType(Long id);

    //通过名字查
    Type getTypeByName(String name);

    //得到全部
    List<Type> listType();

    //按照条件得到多少type(排名最前的size个)
    List<Type> listTypeTop(Integer size);
}
