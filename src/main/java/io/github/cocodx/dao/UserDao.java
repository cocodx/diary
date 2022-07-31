package io.github.cocodx.dao;

import io.github.cocodx.model.User;
import io.github.cocodx.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author amazfit
 * @date 2022-08-01 上午6:25
 **/
public class UserDao {

    /**
     * 根据用户名和密码查询用户信息
     * @param userName
     * @param password
     * @return user
     * @throws Exception
     */
    public User selectOne(Connection connection,String userName,String password) throws Exception {
        User user = new User();
        String sql="select * from t_user where user_name=? and password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,userName);
        preparedStatement.setString(2,password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            user.setUserId(resultSet.getLong("user_id"));
            user.setUserName(userName);
            user.setPassword(password);
            user.setNickName(resultSet.getString("nick_name"));
            user.setImageName(resultSet.getString("image_name"));
            user.setMood(resultSet.getString("mood"));
        }
        return user;
    }
}
