package io.github.cocodx.model;

import lombok.Data;

/**
 * @author amazfit
 * @date 2022-08-04 下午11:37
 **/
@Data
public class DiaryType {

    /** 类型id **/
    private Long typeId;
    /** 日记类型名称 **/
    private String typeName;
    /** 统计日记类型总数 **/
    private Long count;
}
