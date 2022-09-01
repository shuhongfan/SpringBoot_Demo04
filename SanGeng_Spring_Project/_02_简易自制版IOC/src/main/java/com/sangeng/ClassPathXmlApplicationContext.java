package com.sangeng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext  {

    private static Map<String,Object> beanMap = new HashMap<>();
    private String filePath;
    public ClassPathXmlApplicationContext(String filePath){
        this.filePath = filePath;
        load();
    }

    private  void load() {
        try {
            String path = ClassPathXmlApplicationContext.class.getClassLoader().getResource(filePath).getPath();
            path = URLDecoder.decode(path,"utf-8");
            Document document = Jsoup.parse(new File(path), "utf-8");
            Elements beans = document.getElementsByTag("bean");

            if(beans!=null && beans.size()>0 ){
                for (int i = 0; i < beans.size(); i++) {
                    //遍历所有的bean标签
                    Element bean = beans.get(i);
                    //获取 bean标签中的class和id值
                    String className = bean.attr("class");
                    String id = bean.attr("id");

                    //通过反射创建对应类的对象
                    Class<?> clazz = Class.forName(className);
                    Constructor<?> constructor = clazz.getConstructor();
                    Object obj = constructor.newInstance();

                    //把id作为key 创建出来的对象作为value存进map中
                    beanMap.put(id,obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id从容器中获取对应的对象
     * @param id
     * @return
     */
    public Object getBean(String id){
        return beanMap.get(id);
    }
}
