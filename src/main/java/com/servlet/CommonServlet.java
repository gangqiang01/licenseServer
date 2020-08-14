package com.servlet;

import com.former.Response;
import com.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class CommonServlet extends HttpServlet
{
    public static final String headerName = "accesstoken";
    private static final Logger LOG = LoggerFactory.getLogger(CommonServlet.class);

    private boolean isValid(HttpServletRequest httpServletRequest, String token){
        try{
            String uname;
            HttpSession session = httpServletRequest.getSession();
            String username = JwtUtil.getSubject(httpServletRequest, token);
            uname = (String)session.getAttribute("username");
            if(username == null || !uname.equals(username)){
                return false;
            }
        }catch(Exception e){
            LOG.info("jwtError:"+ e);
            return false;
        }

        return true;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type,accesstoken,timeout");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        String mothod  =  "OPTIONS";
        String token = req.getHeader(headerName);
//        LOG.info("request url"+ req.getRequestURI());
        //common valid
        if(isValid(req, token)
                || req.getMethod().equals(mothod)
                ||req.getRequestURI().indexOf("/api/solutionApp/getIconByName") != -1
                ||req.getRequestURI().indexOf("/api/configmgr/downloadConfigFile") != -1
                //screen shot
                ||req.getRequestURI().indexOf("screenshotimg") != -1

        ){


            super.service(req, resp);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().format(Response.error("illegal user").toString()).flush();
        }
    }

}
