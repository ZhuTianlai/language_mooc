package com.imooc.homepage.service;

import com.alibaba.fastjson.JSON;
import com.imooc.homepage.Application;
import com.imooc.homepage.CourseInfosRequest;
import com.imooc.homepage.dao.HomepageCourseDao;
import com.imooc.homepage.entity.HomepageCourse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * <h1>课程服务测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class HomepageCourseServiceTest {

    @Autowired
    private HomepageCourseDao courseDao;

    @Autowired
    private ICourseService courseService;

//    @Test
    public void testCreateCourseInfo() {

        HomepageCourse course1 = new HomepageCourse(
                "JDK11&12 新特性解读",
                0,
                "https://www.imooc.com",
                "解读 JDK11 和 JDK12 的新版本特性"
        );
        HomepageCourse course2 = new HomepageCourse(
                "基于 SpringCloud 微服务架构 广告系统设计与实现",
                1,
                "https://www.imooc.com",
                "广告系统的设计与实现"
        );
        System.out.println(courseDao.saveAll(
                Arrays.asList(course1, course2)
        ).size());
    }

    @Test
    public void testGetCourseInfo() {

        System.out.println(JSON.toJSONString(
                courseService.getCourseInfo(6L)
        ));
        System.out.println(JSON.toJSONString(
                courseService.getCourseInfo(8L)
        ));
    }

    @Test
    public void testGetCourseInfos() {

        // [{"courseIcon":"https://www.imooc.com",
        // "courseIntro":"解读 JDK11 和 JDK12 的新版本特性",
        // "courseName":"JDK11&12 新特性解读","courseType":"免费课程","id":6},
        //
        // {"courseIcon":"https://www.imooc.com",
        // "courseIntro":"广告系统的设计与实现",
        // "courseName":"基于 SpringCloud 微服务架构 广告系统设计与实现",
        // "courseType":"实战课程","id":7}]

        System.out.println(JSON.toJSONString(
                courseService.getCourseInfos(
                        new CourseInfosRequest(Arrays.asList(6L, 7L))
                )
        ));
    }
}
