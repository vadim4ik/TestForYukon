package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vadym on 29.01.2017.
 */
@Service("tcpService")
public class TCPServiceImpl implements TCPService {
    @Autowired
    @Qualifier("applicationProperties")
    private Properties applicationProperties;

    public Set getHosts () {
            ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{
                    "/SpringBeans.xml"});
            Set hosts = (Set) appContext.getBean("hostsList");
            return hosts;
    }

    public boolean verifyHosts () throws InterruptedException {
        Set hosts = getHosts();
        if (hosts.size() > 0) {
            for (Object entry : hosts) {
                String host = (String) ((LinkedHashMap) entry).get("host");
                int port = Integer.parseInt((String)((LinkedHashMap) entry).get("port"));

                TimeUnit.SECONDS.sleep(1);
                Socket skt = setSocketConnection(host, port);
                    if (skt instanceof Socket) {
                        if (skt.isConnected()) {
                            System.out.format("Service in host %s:%d is up\n", host, port);
                            return true;
                        } else {
                            System.out.format("Service in host %s:%d is down\n", host, port);

                        }
                    }
            }
        }
        return false;
    }

    public boolean runServers (){
        Set hosts = getHosts();
        try {
            for (Object entry : hosts)  {
                int port = Integer.parseInt((String)((LinkedHashMap) entry).get("port"));

                ServerSocket srvr = new ServerSocket(port);
                Socket skt = srvr.accept();
                System.out.print("Server has connected!\n");
                PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
                System.out.print("Sending test string: ");
                out.close();
                skt.close();
                srvr.close();
                System.out.print("Server has connected!\n");
                return true;
            }
        }
        catch(Exception e) {
            System.out.print("Error during start servers!\n");
        }
        return false;
    }

    private Socket setSocketConnection(String host, int port ) {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            int timeOut = Integer.parseInt(applicationProperties.getProperty("TIME_OUT"));

            SocketAddress sockaddr = new InetSocketAddress(inetAddress, port);
            Socket skt = new Socket();
            skt.setReuseAddress(true);
            skt.connect(sockaddr, timeOut);
            return skt;
        } catch (Exception e) {
            System.out.format("Service in host %s:%d is down\n", host, port);
        }
        return null;
    }
}
