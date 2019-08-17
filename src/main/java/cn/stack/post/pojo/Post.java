package cn.stack.post.pojo;

import javax.persistence.*;

@Entity
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="post_name")
    private String postname;   //每个post的问题标题
    @Column(name="content")
    private String content;    //每个post的内容
    @Column(name="question_vote")
    private Integer questionvote;   //每个问题的提问投票数
    @Column(name="answer_vote")
    private Integer answervote;     //该问题接受的回答投票数
    @Column(name="url")
    private String url;             //该问题的链接

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getQuestionvote() {
        return questionvote;
    }

    public void setQuestionvote(Integer questionvote) {
        this.questionvote = questionvote;
    }

    public Integer getAnswervote() {
        return answervote;
    }

    public void setAnswervote(Integer answervote) {
        this.answervote = answervote;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postname='" + postname + '\'' +
                ", content='" + content + '\'' +
                ", questionvote=" + questionvote +
                ", answervote=" + answervote +
                ", url='" + url + '\'' +
                '}';
    }
}
