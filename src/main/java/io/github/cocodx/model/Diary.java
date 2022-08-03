package io.github.cocodx.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author amazfit
 * @date 2022-08-03 下午10:21
 **/
@Data
public class Diary implements Serializable {

    /** 日记id **/
    private Long diaryId;
    /** 标题 **/
    private String title;
    /** 内容 **/
    private String content;
    /** 分类id **/
    private Long typeId;
    /** 发布日期 **/
    private Date releaseDate;
}
