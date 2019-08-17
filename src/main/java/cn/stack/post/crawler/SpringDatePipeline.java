package cn.stack.post.crawler;

import cn.stack.post.pojo.Post;
import cn.stack.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDatePipeline implements Pipeline {
    @Autowired
    private PostService postService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的post中的数据，此处的postdata为之前的key值
        Post post=resultItems.get("postdata");
        //判断数据是否不为空
        if(post!=null){
            //如果不为空就把数据保存到数据库中
            this.postService.save(post);
        }
    }
}
