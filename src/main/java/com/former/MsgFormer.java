package com.former;


public class MsgFormer<T> {

    /*提示信息 */
    private String status;

    /*具体内容*/
    private  T data;

//    all list count
    private long count;

    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public T getData(){
        return this.data;
    }
    public void setData(T data){
        this.data = data;
    }

    public long getCount(){
        return this.count;
    }
    public void setCount(long count){
        this.count = count;
    }

}
