package com.imooc.homepage.service.impl;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import com.imooc.homepage.UserInfo;
import com.imooc.homepage.client.CourseClient;
import com.imooc.homepage.dao.HomepageUserCourseDao;
import com.imooc.homepage.dao.HomepageUserDao;
import com.imooc.homepage.entity.HomepageUser;
import com.imooc.homepage.entity.HomepageUserCourse;
import com.imooc.homepage.service.IUserService;
import com.imooc.homepage.vo.CreateUserRequest;
import com.imooc.homepage.vo.UserCourseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>用户相关服务实现</h1>
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final HomepageUserDao homepageUserDao;
    private final HomepageUserCourseDao homepageUserCourseDao;
    private final CourseClient courseClient;

    @Autowired
    public UserServiceImpl(HomepageUserDao homepageUserDao,
                           HomepageUserCourseDao homepageUserCourseDao,
                           CourseClient courseClient) {
        this.homepageUserDao = homepageUserDao;
        this.homepageUserCourseDao = homepageUserCourseDao;
        this.courseClient = courseClient;
    }

    @Override
    public UserInfo createUser(CreateUserRequest request) {

        if (!request.validate()) {
            return UserInfo.invalid();
        }

        HomepageUser oldUser = homepageUserDao.findByUsername(
                request.getUsername()
        );
        if (null != oldUser) {
            return UserInfo.invalid();
        }

        HomepageUser newUser = homepageUserDao.save(
                new HomepageUser(request.getUsername(), request.getEmail())
        );

        return new UserInfo(newUser.getId(),
                newUser.getUsername(), newUser.getEmail());
    }

    @Override
    public UserInfo getUserInfo(Long id) {

        Optional<HomepageUser> user = homepageUserDao.findById(id);
        if (!user.isPresent()) {
            return UserInfo.invalid();
        }

        HomepageUser curUser = user.get();

        return new UserInfo(curUser.getId(),
                curUser.getUsername(), curUser.getEmail());
    }

    @Override
    public UserCourseInfo getUserCourseInfo(Long id) {

        Optional<HomepageUser> user = homepageUserDao.findById(id);
        if (!user.isPresent()) {
            return UserCourseInfo.invalid();
        }

        HomepageUser homepageUser = user.get();
        UserInfo userInfo = new UserInfo(homepageUser.getId(),
                homepageUser.getUsername(), homepageUser.getEmail());

        List<HomepageUserCourse> userCourses =
                homepageUserCourseDao.findAllByUserId(id);
        if (CollectionUtils.isEmpty(userCourses)) {
            return new UserCourseInfo(userInfo, Collections.emptyList());
        }

        List<CourseInfo> courseInfos = courseClient.getCourseInfos(
                new CourseInfosRequest(userCourses.stream()
                .map(HomepageUserCourse::getCourseId)
                .collect(Collectors.toList()))
        );

        return new UserCourseInfo(userInfo, courseInfos);
    }
}
