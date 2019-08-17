package cn.stack.post.dao;

import cn.stack.post.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;
//使用jpa进行数据库映射，第一个参数为实例，第二个参数为主键
public interface PostDao extends JpaRepository<Post,Long> {
}
