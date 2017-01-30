package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vadym on 29.01.2017.
 */
@Service("tcpService")
public class TCPServiceImpl implements TCPService {
    @Autowired
    @Qualifier("applicationProperties")
    private Properties applicationProperties;

    public List getHosts () {
            ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{
                    "/SpringBeans.xml"});
            List hosts = (List) appContext.getBean("hostsList");
            return hosts;
    }

    public boolean verifyHosts (){
        List hosts = getHosts();
        if (hosts.size() > 0) {
            for (int i = 0; i <= hosts.size() - 1; i++) {
                    Map<String, String> map = (Map<String, String>) hosts.get(i);
                    String host = map.get("host");
                    int port = Integer.parseInt(map.get("port"));

                    Socket skt = setSocketConnection(host, port);
                    if (skt instanceof Socket) {
                        if (skt.isConnected()) {
                            System.out.format("Service in host %s:%d is up\n", host, port);
                            return true;
                        } else {
                            System.out.format("Service in host %s:%d is up\n", host, port);

                        }
                    }
            }
        }
        return false;
    }

    public boolean runServers (){
        List hosts = getHosts();
        try {
            for (int i = 0; i <= hosts.size() - 1; i++) {
                Map<String,String> map = (Map<String, String>) hosts.get(i);
                int port = Integer.parseInt(map.get("port"));

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
