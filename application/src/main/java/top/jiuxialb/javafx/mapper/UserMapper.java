package top.jiuxialb.javafx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.jiuxialb.javafx.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus已提供常用的CRUD方法
    // 可以在此处添加自定义的查询方法
}