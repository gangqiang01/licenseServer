package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.in;

/**
 * Created by root on 19-7-24.
 */
public class SystemUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SystemUtil.class);

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }
    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }
    public static String selectNetName(){
        String str = null;
        String[] anames = new String[20];
        String command = "ls /sys/class/net/";
        try {
            Process pp = Runtime.getRuntime().exec(command);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(pp.getInputStream()), 7777);
            BufferedReader stderr = new BufferedReader(new InputStreamReader(pp.getErrorStream()), 7777);
            int i = 0;
            while ((null != (str = stdout.readLine())) || (null != (str = stderr.readLine()))) {
                if(i > 19)
                    break;
                anames[i] = str.trim();
                i++;
            }
            for(String s: anames){
                //LOG.info("name:" + s);
                if(s.contains("docker"))
                    continue;
                if(s.contains("lo"))
                    continue;
                if(s.contains("br-"))
                    continue;
                if(s.contains("veth"))
                    continue;

                if(s.contains("eth") || s.contains("enp") || s.contains("ens"))
                    return s;
            }
            return null;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static String getMACAddressFromSys(String name) {
        String str = null;
        String macSerial = null;
        String command = "cat /sys/class/net/" + name + "/address";
        if(name == null || name.equals(""))
            return null;
        try {
            //LOG.info("name:" + name);
            Process pp = Runtime.getRuntime().exec(command);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            str = input.readLine();
            if (str != null) {
                macSerial = str.trim();
            }
            if(macSerial == null || macSerial.equals("")) {
                return null;
            } else {
                if(macSerial.length() != 17)
                    return null;
                else {
                    //LOG.info("macadress:" + macSerial);
                    return macSerial;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //get localhost ip
    public static String getLocalhostIp() throws SocketException {
        String hostname = null;
        String cardName = getValidNetworkCardNameFromSys();
        LOG.info("networkCardName:"+cardName);
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)){
            if(netint.getName().trim().equals(cardName)){
                hostname = getHostname(netint);
            }
        }
        return hostname;
    }

    public static String getValidNetworkCardNameFromSys() {
        String command = "cat /proc/net/route|awk 'NR==2 {print $1}'";
        try {
            Process pp = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", command});
            InputStream in = pp.getInputStream();
            byte[] b = new byte[4096];
            StringBuffer result = new StringBuffer();
            while(true){
                int n = in.read(b);
                if(n == -1)
                    break;
                result.append(new String(b, 0, n).trim());
            }
            pp.waitFor();
            pp.destroy();

            if(result == null || result.equals("")) {
                return null;
            } else {
                return result.toString();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }finally {
            try {
                in.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    static String getHostname(NetworkInterface netint) throws SocketException {
        String hostname = null;
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if(!inetAddress.isLinkLocalAddress() && (inetAddress.getHostAddress()).indexOf(":")==-1){
                hostname = inetAddress.getHostAddress();
            }
        }
        return hostname;
    }
}
