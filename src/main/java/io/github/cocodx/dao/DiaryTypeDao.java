package io.github.cocodx.dao;

import com.apifan.common.random.source.NumberSource;
import com.apifan.common.random.source.OtherSource;
import io.github.cocodx.model.DiaryType;
import io.github.cocodx.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amazfit
 * @date 2022-08-04 下午11:39
 **/
public class DiaryTypeDao {

    /**
     * 表中添加测试数据
     * @param connection
     * @throws SQLException
     */
    public void insertDiaryType(Connection connection) throws SQLException {
        long diaryTypeId = NumberSource.getInstance().randomLong(1, 100);
        String diaryTypeStr = OtherSource.getInstance().randomChineseIdiom();
        String sql="insert into t_diary_type (type_id,type_name) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,diaryTypeId);
        preparedStatement.setString(2,diaryTypeStr);
        preparedStatement.execute();
    }

    public static void main(String[] args) throws Exception {
        Connection connection = DbUtil.connection();
        for (int i = 0; i < 6; i++) {
            long diaryTypeId = NumberSource.getInstance().randomLong(100, 1000);
            String diaryTypeStr = OtherSource.getInstance().randomChineseIdiom();
            String sql="insert into t_diary_type (type_id,type_name) values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,diaryTypeId);
            preparedStatement.setString(2,diaryTypeStr);
            preparedStatement.execute();
        }
    }

    public List<DiaryType> selectDiaryTypeCount(Connection connection) throws SQLException {
        String sql = "SELECT dt.*,count(d.diary_id) as count FROM `t_diary_type` dt\n" +
                "LEFT JOIN t_diary d ON d.type_id=dt.type_id GROUP BY dt.type_id;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DiaryType> list = new ArrayList<>();
        while (resultSet.next()){
            DiaryType diaryType = new DiaryType();
            diaryType.setTypeId(resultSet.getLong("type_id"));
            diaryType.setTypeName(resultSet.getString("type_name"));
            diaryType.setCount(resultSet.getLong("count"));
            list.add(diaryType);
        }
        return list;
    }

    public List<DiaryType> selectList(Connection connection) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select * from t_diary_type");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DiaryType> list = new ArrayList<>();
        while (resultSet.next()){
            DiaryType diaryType = new DiaryType();
            diaryType.setTypeId(resultSet.getLong("type_id"));
            diaryType.setTypeName(resultSet.getString("type_name"));
            list.add(diaryType);
        }
        return list;
    }

    public void deleteByTypeId(String typeId,Connection connection) throws SQLException {
        String sql = "delete from t_diary_type where type_id = "+typeId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }
}
