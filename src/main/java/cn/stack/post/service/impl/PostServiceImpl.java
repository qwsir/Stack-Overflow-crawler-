package cn.stack.post.service.impl;

import cn.stack.post.dao.PostDao;
import cn.stack.post.pojo.Post;
import cn.stack.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

    @Transactional   //对数据库操作时属于事务操作
    public void save(Post post) {
        //查询原有的数据，防止出现重复数据
        Post param=new Post();
        param.setUrl(post.getUrl());
        param.setPostname(post.getPostname());

        //判断数据库中是否有已存在的数据
        List<Post> list= this.findPost(param);

        //判断数据是否为空
        if(list.size()==0){
            //如果查询结果不为空，表示post信息不存在
            //则表示需要新增或者新增数据
            this.postDao.saveAndFlush(post);
        }

    }

    @Override
    public List<Post> findPost(Post post) {
        //设置查询条件
        Example example = Example.of(post);
        List list=this.postDao.findAll(example);
        return list;
    }
}
