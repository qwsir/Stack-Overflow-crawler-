package cn.stack.post.service;

import cn.stack.post.pojo.Post;

import java.util.List;

public interface PostService {
    /**
     * 保存当前的post数据
     * @param post
     */
    public void save(Post post);

    /**
     * 根据条件查找post信息
     * @param post
     * @return
     */
    public List<Post> findPost(Post post);
}
