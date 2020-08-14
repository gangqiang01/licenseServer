package com.licensemgr;

import com.db.entity.LicenseInfo;
import com.db.server.LicenseInfoServer;
import com.servlet.LicenseServlet;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.naming.ldap.UnsolicitedNotificationEvent;
import java.io.File;
/**
 * @date ：Created in 8/13/20 9:58 AM
 * @description：license server
 */
public class LicenseServer {
    private Server server;
    private static final Logger LOG = LoggerFactory.getLogger(LicenseServer.class);
    public static void main(String[] args) {
        new LicenseServer().start();
    }

    private void start(){
        LicenseInfoServer licenseInfoServer = new LicenseInfoServer();
        if(licenseInfoServer.getLicenseInfoByName("test") == null){
            LOG.info("license is null");
            LicenseInfo licenseInfo = new LicenseInfo();
            licenseInfo.setName("test");
            licenseInfo.setNum(2);
            licenseInfo.setProductname("androiddm");
            boolean res = licenseInfoServer.insertLicenseInfo(licenseInfo);
            LOG.info("insert license:"+res);
            LicenseInfo licenseInfo1 = licenseInfoServer.getLicenseInfoByName("test");
            licenseInfo1.setProductname("deviceon");
            boolean res1 = licenseInfoServer.updateLicenseInfo(licenseInfo1);
            LOG.info("res1="+res1);
        }else{
            LOG.info("delete license");
            boolean resu = licenseInfoServer.deleteLicenseInfoByName("test");
            LOG.info("delete license result:"+resu);

        }


        startJettyServer();
    }

    private void startJettyServer() {
        // now prepare and start jetty
        String webPort = System.getenv("PORT");
        if (webPort == null) {
            webPort = "8080";
        }

        String appType = "webapp";
        LOG.info("appType is " +  appType);
        // Setup Threadpool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(400);
        server = new Server(threadPool);

        // define our Connector
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);
        ServerConnector http = new ServerConnector(server,
                new HttpConnectionFactory(http_config));
        http.setPort(Integer.parseInt(webPort));
        http.setIdleTimeout(30000);
        server.setConnectors(new Connector[] { http });

        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
//        root.setResourceBase(this.getClass().getClassLoader().getResource(appType).toExternalForm());
        root.setResourceBase(appType);
        root.setParentLoaderPriority(true);
        server.setHandler(root);

        // Create Servlet


        ServletHolder licenseServletHolder = new ServletHolder(new LicenseServlet());
        root.addServlet(licenseServletHolder, "/api/licensemgr/*");

        // Start jetty
        try {
            server.start();
        } catch (Exception e) {
            LOG.error("jetty error", e);
        }
    }
}
