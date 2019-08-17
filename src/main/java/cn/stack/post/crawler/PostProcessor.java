package cn.stack.post.crawler;

import cn.stack.post.pojo.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class PostProcessor implements PageProcessor {
   private String url="https://stackoverflow.com/questions/tagged/android?tab=votes&page=238&pagesize=15";
    @Override
    public void process(Page page) {
        //获取解析页面的所有链接    (id为# class为.)
        List<Selectable> list = page.getHtml().css("div#questions div.question-summary").nodes();
//        for (Selectable selectable : list) {
//            System.out.println(selectable.links().toString());
//        }
        //判断获取到的集合是否为空
        if(list.size()==0){
            //如果为空，则该页面为post详情页，获取post相关信息
            this.savePost(page);
        }
        else{
            //如果不为空，则这是列表页，解析出当前页的url地址，放到任务队列
            for (Selectable selectable : list) {
                String postUrl=selectable.links().toString();
                page.addTargetRequest(postUrl);
            }
            // 没有id和class的情况下可以使用属性选择器
            String postNextUrl=page.getHtml().css("div.pager a[rel=next]").links().toString();
            System.out.println("----------->"+postNextUrl);
            page.addTargetRequest(postNextUrl);
        }
    }

    //解析页面，获取post页面详情信息
    public void savePost(Page page){
        //创建post对象，对爬取post信息存取
        Post post=new Post();

        //解析页面
        Html html=page.getHtml();
        //获取页面中不同元素值
        Document doc = Jsoup.parse(html.toString());
        Elements elements = doc.getElementsByClass("accepted-answer");
        if (!elements.isEmpty()){
            post.setPostname(html.css("div#question-header h1[itemprop=name] a","text").toString());
            System.out.println("这是问题的题目"+post.getPostname());
            post.setUrl(page.getUrl().toString());
            System.out.println("这是问题的网页地址"+page.getUrl());
            post.setQuestionvote(Integer.valueOf(html.css("div.question div[itemprop=upvoteCount]","text").toString()));
            System.out.println("这是网页的问题投票数"+post.getQuestionvote());
            post.setAnswervote(Integer.valueOf(html.css("div[itemprop=acceptedAnswer] div[itemprop=upvoteCount]","text").toString()));
            System.out.println("这是得到认可的答案的投票"+post.getAnswervote());
            String question = doc.select("div.question div.post-text").text();
            System.out.println("问题的内容为："+question);
            String answer = doc.select("div[itemprop=acceptedAnswer] div.post-text").text();
            System.out.println("回答的内容为："+answer);
            post.setContent(question+answer);
            page.putField("postdata",post);
        }

    }
    private Site site=Site.me()
            .setCharset("utf8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
            .setRetryTimes(3)   //重试次数
            .setTimeOut(10*1000) //超时时间
            .setRetrySleepTime(3*1000);  //重试时间


    @Override
    public Site getSite() {
        return site;
    }
    @Autowired
    private SpringDatePipeline springDatePipeline;
    //initialDelay：当定时任务启动后隔多久启动方法
    //fixedDelay：每隔多久执行方法
    @Scheduled(initialDelay = 1000,fixedDelay = 10*1000)
    public void process(){
        //代理http://www.xicidaili.com     https://proxy.mimvp.com/free.php
        //创建下载器Downloader
        HttpClientDownloader httpClientDownloader=new HttpClientDownloader();
        //给下载器设置代理服务器信息
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("117.127.16.208",8080)
//                new Proxy("221.229.252.98",8080),
//                new Proxy("60.13.42.109",9999),new Proxy("120.83.105.175",9999),
//                new Proxy("163.204.246.29",9999)
        ));

        Spider.create(new PostProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .thread(10)
                .addPipeline(this.springDatePipeline)
//                .setDownloader(httpClientDownloader)   //设置下载器
                .run();
        {

//            while(L->next!=null){
//                if(L->next->data==x){
//                    L->next=L->next->next;
//                }
//                else{
//                    L=L->next;
//                }
//            }
            /**
             * 首先第一点：如果没有Lnode *q=L,那么else 那里的q=q->next这里会报错
             * 第二点：方法传入的是一个地址，对地址进行的操作会改变原来地址中存放的数据。
             * 例如L=L->next会导致链表的长度缩短，这也就是为什么需要引入中间变量来存储这一地址
             * 第三点：如果你认为这里delete方法中删除数据的操作对原来的链表没有改变的话，那么这个操作没有任何意义
             * 因为这里的删除操作就是要在原来链表的基础上进行操作。
             */
        }
    }
}
