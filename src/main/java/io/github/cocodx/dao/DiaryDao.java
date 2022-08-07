package io.github.cocodx.dao;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.apifan.common.random.source.OtherSource;
import io.github.cocodx.model.Diary;
import io.github.cocodx.model.MarkMonthData;
import io.github.cocodx.model.PageBean;
import io.github.cocodx.model.vo.DiaryVo;
import io.github.cocodx.utils.DateUtils;
import io.github.cocodx.utils.DbUtil;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amazfit
 * @date 2022-08-03 下午10:35
 **/
public class DiaryDao {

    public List<Diary> selectList(Connection connection, DiaryVo pageBean) throws SQLException, ParseException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" SELECT d.*,dt.type_name FROM `t_diary` d\n" +
                " INNER JOIN `t_diary_type` dt on d.type_id = dt.type_id ");
        if (pageBean.getTypeId() != null) {
            stringBuffer.append(" where d.type_id = ?");
        }
        if (pageBean.getQueryMonth()!=null){
            stringBuffer.append(" where DATE_FORMAT(d.release_date,'%Y-%m') = ?");
        }
        stringBuffer.append(" order by d.release_date desc ");
        stringBuffer.append(" limit ?,?");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        if (pageBean.getTypeId() != null) {
            preparedStatement.setLong(1,pageBean.getTypeId());
            preparedStatement.setLong(2,pageBean.getStart());
            preparedStatement.setLong(3,pageBean.getSize());
        }
        else if (pageBean.getQueryMonth()!=null){
            preparedStatement.setString(1,pageBean.getQueryMonth());
            preparedStatement.setLong(2,pageBean.getStart());
            preparedStatement.setLong(3,pageBean.getSize());
        }
        else{
            preparedStatement.setLong(1,pageBean.getStart());
            preparedStatement.setLong(2,pageBean.getSize());
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Diary> diaries = new ArrayList<>();
        while (resultSet.next()){
            Diary diary = new Diary();
            diary.setDiaryId(resultSet.getLong("diary_id"));
            diary.setTitle(resultSet.getString("title"));
            diary.setContent(resultSet.getString("content"));
            diary.setReleaseDate(DateUtils.parseDay(resultSet.getString("release_date")));
            diary.setTypeId(resultSet.getLong("type_id"));
            diaries.add(diary);
        }
        return diaries;
    }

    public Long selectCount(Connection connection,DiaryVo diaryVo)throws Exception{
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" SELECT count(*) FROM `t_diary` d\n" +
                "INNER JOIN `t_diary_type` dt on d.type_id = dt.type_id ");
        if (diaryVo.getTypeId()!=null){
            stringBuffer.append(" where d.type_id=?");
        }
        if (diaryVo.getQueryMonth()!=null){
            stringBuffer.append(" where DATE_FORMAT(d.release_date,'%Y-%m') = ?");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        if (diaryVo.getTypeId()!=null){
            preparedStatement.setLong(1,diaryVo.getTypeId());
        }
        if (diaryVo.getQueryMonth()!=null){
            preparedStatement.setString(1,diaryVo.getQueryMonth());
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        Long count = 0L;
        while (resultSet.next()){
            count = resultSet.getLong("count(*)");
        }
        return count;
    }

    /**
     * 获取日志日期
     * @param connection
     * @return
     * @throws SQLException
     */
    public List<MarkMonthData> selectMonthDataCount(Connection connection) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select mark_month,count(mark_month) as mark_number from\n" +
                "(SELECT DATE_FORMAT(release_date,'%Y-%m') AS mark_month FROM `t_diary`) table2\n" +
                "GROUP BY table2.mark_month ORDER BY table2.mark_month desc;");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MarkMonthData> list = new ArrayList<>();
        while (resultSet.next()){
            MarkMonthData markMonthData = new MarkMonthData();
            markMonthData.setDataMonth(resultSet.getString("mark_month"));
            markMonthData.setDataNumber(resultSet.getLong("mark_number"));
            list.add(markMonthData);
        }
        return list;
    }


    public void insertData(Connection connection) throws SQLException {
        long diaryTypeId = NumberSource.getInstance().randomLong(1, 100);
        String diaryTypeStr = OtherSource.getInstance().randomChineseIdiom();
        String sql="insert into t_diary_type (type_id,type_name) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,diaryTypeId);
        preparedStatement.setString(2,diaryTypeStr);
        preparedStatement.execute();

        ArrayList<Diary> diaries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Diary diary = new Diary();
            diary.setTitle(OtherSource.getInstance().randomChinese(6));
            diary.setContent(OtherSource.getInstance().randomChinese(50));
            diary.setTypeId(diaryTypeId);
            diary.setReleaseDate(DateTimeSource.getInstance().randomDate(2020));
            diaries.add(diary);
        }

        String sql1 = "insert into t_diary (diary_id,title,content,type_id,release_date) values (null,?,?,?,?)";
        diaries.forEach(item->{
            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                preparedStatement1.setString(1,item.getTitle());
                preparedStatement1.setString(2,item.getContent());
                preparedStatement1.setLong(3,item.getTypeId());
                preparedStatement1.setDate(4,new Date(item.getReleaseDate().getTime()));
                preparedStatement1.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
        System.out.println("测试插入成功");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = DbUtil.connection();
        DiaryDao diaryDao = new DiaryDao();
        diaryDao.insertData(connection);
    }


}
