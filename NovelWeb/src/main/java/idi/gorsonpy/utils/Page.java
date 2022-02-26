package idi.gorsonpy.utils;


import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

//分页信息的封装类
@Data
public class Page<T> implements Serializable {
    //状态码
    private Integer status;
    //状态
    private String message;
    //小说当前页码
    private int pageNum;
    //一页展示的条数
    private int pageSize;
    //总条数
    private long pageTotal;
    //总页数
    private int pages;
    //上一页页码
    private int prePage;
    //下一页页码
    private int nextPage;
    //是否是第一页
    private boolean isFirstPage;
    //是否是最后一页
    private boolean isLastPage;
    //返回封装数据
    private T data;


    public Page() {

    }

    public Page(Integer status) {
        this.status = status;
    }

    public Page(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Page(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //不返回数据构造方法
    public Page(CodeEnum codeEnum) {
        this.status = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    //返回数据构造方法
    public Page(CodeEnum codeEnum, T data) {
        this(codeEnum);
        this.data = data;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //设置页数信息
    public void setPageInfo(PageInfo pageInfo){
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.pageTotal = pageInfo.getTotal();
        this.pages = pageInfo.getPages();
        this.prePage = pageInfo.getPrePage();
        this.nextPage = pageInfo.getNextPage();
        this.isFirstPage = pageInfo.isIsFirstPage();
        this.isLastPage = pageInfo.isIsLastPage();
    }

    //请求成功（不返回数据）
    public static <T> Page<T> success() {
        return new idi.gorsonpy.utils.Page<T>(CodeEnum.SUCCESS);
    }

    //请求成功（返回数据）
    public static <T> Page<T> success(T data) {
        return new idi.gorsonpy.utils.Page<T>(CodeEnum.SUCCESS, data);
    }

    //参数格式不正确
    public static <T> Page<T> badRequest() {
        return new idi.gorsonpy.utils.Page<T>(CodeEnum.BAD_REQUEST);
    }

    //  .......可根据自己的需要往下延伸


}
