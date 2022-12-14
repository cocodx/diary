package io.github.cocodx.dao;

import com.apifan.common.random.source.DateTimeSource;
import com.apifan.common.random.source.NumberSource;
import com.apifan.common.random.source.OtherSource;
import com.apifan.common.random.util.DataUtils;
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
        if (pageBean.getDiaryTitle()!=null){
            stringBuffer.append(" where d.title like '%"+pageBean.getDiaryTitle()+"%'");
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
        else if (pageBean.getDiaryTitle()!=null){
            preparedStatement.setLong(1,pageBean.getStart());
            preparedStatement.setLong(2,pageBean.getSize());
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
        if (diaryVo.getDiaryTitle()!=null){
            stringBuffer.append(" where d.title like '%"+diaryVo.getDiaryTitle()+"%'");
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

    public Diary selectOne(Connection connection,String diaryId) throws SQLException, ParseException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT d.diary_id,d.title,d.content,d.release_date,dt.type_id,dt.type_name FROM t_diary d \n" +
                "INNER JOIN t_diary_type dt ON d.type_id = dt.type_id where d.diary_id = ?");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        preparedStatement.setString(1,diaryId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Diary diary = new Diary();
        while (resultSet.next()){
            diary.setDiaryId(resultSet.getLong("diary_id"));
            diary.setTitle(resultSet.getString("title"));
            diary.setContent(resultSet.getString("content"));
            diary.setReleaseDate(resultSet.getDate("release_date"));
            diary.setTypeId(resultSet.getLong("type_id"));
            diary.setTypeName(resultSet.getString("type_name"));
        }
        return diary;
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

    /**
     * 插入日记数据
     * @param connection
     * @param title
     * @param content
     * @param typeId
     * @throws SQLException
     */
    public void insert(Connection connection, String title, String content, String typeId) throws SQLException {
        String sql = "insert into t_diary (diary_id,title,content,type_id,release_date) values (null,?,?,?,now())";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,title);
        preparedStatement.setString(2,content);
        preparedStatement.setLong(3,Long.parseLong(typeId));
        preparedStatement.execute();
        preparedStatement.close();
    }

    /**
     * 删除日记数据
     * @param connection
     * @param diaryId
     */
    public void delete(Connection connection, String diaryId) throws SQLException {
        String sql = "delete from t_diary where diary_id="+diaryId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    /**
     * 更新日记
     * @param connection
     * @param title
     * @param content
     * @param typeId
     * @param diaryId
     */
    public void update(Connection connection, String title, String content, String typeId, String diaryId) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("update t_diary set title=?,content=?,type_id=? where diary_id=?");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuffer.toString());
        preparedStatement.setString(1,title);
        preparedStatement.setString(2,content);
        preparedStatement.setString(3,typeId);
        preparedStatement.setString(4,diaryId);
        preparedStatement.execute();
        preparedStatement.close();
    }

    /**
     * 根据typeId 查找 数据条数
     * @param typeId
     * @param connection
     * @return
     */
    public Integer selectCountByTypeId(String typeId, Connection connection) throws SQLException {
        String sql = "select count(*) as data_count from t_diary where type_id = "+typeId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Integer result = null;
        while (resultSet.next()){
            result = resultSet.getInt("data_count");
        }
        return result;
    }
}
