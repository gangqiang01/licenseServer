package com.former;


import com.alibaba.fastjson.JSONObject;

public class Response {
    /**
     * success
     * @param object
     * @return
     */
    public static JSONObject success(Object object){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("success");
        msg.setData(object);
        return (JSONObject) JSONObject.toJSON(msg);
    }

    public static JSONObject success(Object object, long count){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("success");
        msg.setData(object);
        msg.setCount(count);
        return (JSONObject) JSONObject.toJSON(msg);
    }
    public static JSONObject success(){
        return success(null);
    }

    public static JSONObject error(String resultmsg){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("error");
        msg.setData(resultmsg);
        return (JSONObject) JSONObject.toJSON(msg);
    }


}
