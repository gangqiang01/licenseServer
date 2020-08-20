package com.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @date ：Created in 5/20/19 4:31 PM
 * @description：
 */
public class ParseRequestBody {
    private static final Logger LOG = LoggerFactory.getLogger(ParseRequestBody.class);
    private HttpServletRequest req;
    private Map<String, String> parameters = new HashMap<String, String>();
    // "{id:2, value:n false}"
    private String jsonContent = null;
    public ParseRequestBody(HttpServletRequest req){
        this.req = req;
    }
    public  String getReqBodyValue(String rParameters) throws IOException{
        // query parameter get null
        String contentType = HttpFields.valueParameters(this.req.getContentType(), parameters);

        //get query string ?a=a&b=b
        if(contentType == null){
            String queryString = this.req.getQueryString();
            if(queryString != null|| !queryString.equals("")){
                JSONObject json = splitString(queryString);
                this.jsonContent  = json.toString();
                if(json.containsKey(rParameters))
                    return json.getString(rParameters);
            }
        }else if("application/x-www-form-urlencoded".equals(contentType)){
            return this.req.getParameter(rParameters);
        }else if("application/json".equals(contentType)){
            if(this.jsonContent == null){
                //The second execution get null
                this.jsonContent = IOUtils.toString(this.req.getInputStream(), parameters.get("charset"));
            }

            try {
                String value = JSONObject.parseObject(this.jsonContent).getString(rParameters);
                return value;
            }catch (NullPointerException e){
                e.printStackTrace();
                LOG.error(rParameters+" dose not exist in "+ this.jsonContent);
            }catch(Exception e){
                e.printStackTrace();
                LOG.error("[ParseRequestBody]exception: "+e.toString());
            }
        }

        LOG.error("[getReqBodyValue]Not support request content type");
        return null;
    }

    public String getReqJsonContent() throws IOException{
        String contentType = HttpFields.valueParameters(this.req.getContentType(), parameters);
        if(this.jsonContent == null){
            //The second execution get null
            if(contentType == null){
                String queryString = this.req.getQueryString();
                if(queryString != null|| !queryString.equals("")){
                    JSONObject json = splitString(queryString);
                    this.jsonContent = json.toString();
                }
            }else if("application/json".equals(contentType)){
                this.jsonContent = IOUtils.toString(this.req.getInputStream(), parameters.get("charset"));
            }else{
                LOG.error("[getReqJsonContent]Not support request content type");
                return null;
            }

        }
        return this.jsonContent;
    }



    public JSONObject splitString(String s) {
        // String s represents ParamAndMacro
        String[] split = s.split("=|\\&");
        int length = split.length;
        JSONObject json = new JSONObject();
        for (int i=0; i<length; i+=2){
            String value = "";
            if(i+1 < length){
                value = split[i+1];
            }
            json.put(split[i], value);
        }
        return json;
    }

}
