package com;

/**
 * Created by Vadym on 30.01.2017.
 */
import com.services.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

class TCPServer {
     public static void main(String args[]) {
         ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{
                 "/SpringBeans.xml"});
         List hosts = (List) appContext.getBean("hostsList");

         try {
             for (int i = 0; i <= hosts.size() - 1; i++) {
                 Map<String, String> map = (Map<String, String>) hosts.get(i);
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
             }
         } catch (Exception e) {
             System.out.print("Error during start servers!\n");
         }
     }
}