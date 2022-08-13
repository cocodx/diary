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
        User user = null;
        String sql="select * from t_user where user_name=? and password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,userName);
        preparedStatement.setString(2,password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            user = new User();
            user.setUserId(resultSet.getLong("user_id"));
            user.setUserName(userName);
            user.setPassword(password);
            user.setNickName(resultSet.getString("nick_name"));
            user.setImageName(resultSet.getString("image_name"));
            user.setMood(resultSet.getString("mood"));
        }
        return user;
    }

    /**
     * 更新 用户信息
     * @param connection
     * @param user
     */
    public void update(Connection connection, User user) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("update t_user set nick_name=?,mood=?,image_name=? where user_id=?");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        preparedStatement.setString(1,user.getNickName());
        preparedStatement.setString(2,user.getMood());
        preparedStatement.setString(3, user.getImageName());
        preparedStatement.setLong(4,user.getUserId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
