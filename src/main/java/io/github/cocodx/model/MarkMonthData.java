package io.github.cocodx.model;

import lombok.Data;

import java.util.Date;

/**
 * @author amazfit
 * @date 2022-08-06 下午3:30
 **/
@Data
public class MarkMonthData {

    /** 日记发布的月份 **/
    private String dataMonth;

    /** 日记发布的月份，此月的日记数量 **/
    private Long dataNumber;
}
