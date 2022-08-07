package io.github.cocodx.model;

import lombok.Data;

/**
 * @author amazfit
 * @date 2022-08-04 上午6:21
 **/
@Data
public class PageBean {

    private Long page=1L;
    private Long size=10L;
    private Long start;
    private Long total;

    /** 总共页数 **/
    private Long totalPage;

    public Long getStart() {
        return (page-1)*size;
    }

    public Long getTotalPage(){
        if (total<=size){
            return 1L;
        }
        return total%size==0 ? total/size : total/size+1;
    }

    public Boolean hasPrev(){
        if (page==1L){
            return false;
        }
        return true;
    }

    public Boolean hasNext(){
        Long calTotal = page*size;
        if (calTotal<total){
            return true;
        }else {
            return false;
        }
    }


    public static void main(String[] args) {
        PageBean pageBean = new PageBean();
        pageBean.setPage(1L);
        pageBean.setSize(5L);
        pageBean.setTotal(7L);
        System.out.println(pageBean.getTotalPage());
    }
}
