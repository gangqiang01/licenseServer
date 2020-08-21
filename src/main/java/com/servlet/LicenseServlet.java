package com.servlet;

import com.alibaba.fastjson.JSONObject;
import com.db.entity.LicenseInfo;
import com.db.server.LicenseInfoServer;
import com.former.Response;
import com.licensemgr.LicenseServer;
import com.util.CreateAuthCode;
import com.util.ParseRequestBody;
import com.util.ThirtySixToTenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @date ：Created in 8/13/20 10:00 AM
 * @description：license info
 */
public class LicenseServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LicenseServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] path = StringUtils.split(req.getPathInfo(), '/');
        ParseRequestBody parseRequestBody = new ParseRequestBody(req);
        int length = 0;
        if(path == null)
            length = 0;
        else
            length = path.length;
        switch (length) {
            case 0:
                get_1_createAuthcode(req, resp, parseRequestBody);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
                return;
        }
    }

    private void get_1_createAuthcode(HttpServletRequest req, HttpServletResponse resp, ParseRequestBody parseRequestBody) throws ServletException, IOException {
        String pn = parseRequestBody.getReqBodyValue("partnumber");
        String machineId = parseRequestBody.getReqBodyValue("machineID");
        String authcode;
        if(StringUtils.isBlank(pn)|| StringUtils.isBlank(machineId)){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().format(Response.error("Parameter error").toString()).flush();
            return;
        }
        LicenseInfoServer licenseInfoServer = new LicenseInfoServer();
        LicenseInfo licenseInfo = licenseInfoServer.getLicenseInfoByMachineIdAndProductname(machineId, pn);
        if(licenseInfo != null){
            authcode = String.valueOf(licenseInfo.getAuthcode());
        }else{
            Calendar now = Calendar.getInstance();
            String y, m, d;
            y = String.valueOf(now.get(Calendar.YEAR));
            m = String.valueOf(now.get(Calendar.MONTH)+1);
            d = String.valueOf(now.get(Calendar.DATE));
            if(m.length() == 1){
                m = String.format("0%s", m);
            }
            if(d.length() == 1){
                d = String.format("0%s", d);
            }
            String dateString = String.format("%1$s%2$s%3$s%4$s", y, m, d, "01");
            CreateAuthCode createAuthCode = new CreateAuthCode();
            String encodeStr = String.format("%1$s+%2$s+%3$s", pn, machineId, dateString);
            String thirtySixDate = ThirtySixToTenUtil.DeciamlToThirtySix(Integer.valueOf(dateString));
            LOG.info("thirtySix:"+thirtySixDate+"ten:"+dateString);
            authcode = createAuthCode.GetAuthCodeWithTime(encodeStr, thirtySixDate);
            LicenseInfo licenseInfo1 = new LicenseInfo();
            licenseInfo1.setProductname(pn);
            licenseInfo1.setAuthcode(authcode);
            licenseInfo1.setMachineid(machineId);
            licenseInfoServer.insertLicenseInfo(licenseInfo1);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("authcode", authcode);
        resp.setContentType("application/json");
        resp.getOutputStream().write(Response.success(jsonObject).toString().getBytes("UTF-8"));
        resp.setStatus(HttpServletResponse.SC_OK);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
