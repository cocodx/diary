package io.github.cocodx.model.vo;

import io.github.cocodx.model.PageBean;
import lombok.Data;

/**
 * @author amazfit
 * @date 2022-08-06 下午2:30
 **/
@Data
public class DiaryVo extends PageBean {

    /** 日志类别id **/
    private Long typeId;

    /** 日志日期-月份 **/
    private String queryMonth;

    /** 日记名称-模糊搜索 **/
    private String diaryTitle;
}
