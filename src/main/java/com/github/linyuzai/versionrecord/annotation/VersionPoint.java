package com.github.linyuzai.versionrecord.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(VersionPoints.class)
public @interface VersionPoint {

    /**
     * 如定义了版本号，可以指定某个版本号
     *
     * @return 版本号
     */
    String version() default "";

    /**
     * 对于代码的描述，新增功能或修改功能
     *
     * @return 描述
     */
    String description();

    /**
     * 当前的分支
     *
     * @return 分支
     */
    String branch();

    /**
     * 编写的人
     *
     * @return 作者
     */
    String author();

    /**
     * 该新增或修改会需要其他服务同步修改
     *
     * @return 需要依赖的服务
     */
    String[] dependServices() default {};

    /**
     * 该新增或修改会需要数据库表的新增和修改
     *
     * @return 需要依赖的表
     */
    String[] dependTables() default {};

    /**
     * 时间，默认格式：yyyy-MM-dd
     *
     * @return 添加时间
     */
    String date();
}
