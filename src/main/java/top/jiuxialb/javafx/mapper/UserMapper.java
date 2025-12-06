package top.jiuxialb.javafx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.jiuxialb.javafx.entity.User;
import org.apache.ibatis.annotations.Mapper;

// 用户Mapper接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
}