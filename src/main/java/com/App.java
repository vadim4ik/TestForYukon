package com;

import com.services.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class App  {

	public static void main(String args[]) {
		try {
			ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{
					"/SpringBeans.xml"});
            App p = (App) appContext.getBean("app",App.class);
            p.start(args);
		}
		catch(Exception e) {
			System.out.print("Error in Application Context!\n");
		}
	}

	@Autowired
	private TCPService tcpService;

	private class runServers implements Runnable {
            public void run() {
                    tcpService.runServers();
            }
    }

    private void start(String[] args) {
        Thread t = new Thread(new runServers());
        t.setPriority(10);
        t.start();
		try {
		    while (true) {
		        tcpService.verifyHosts();
            }
		}
		catch(Exception e) {
			System.out.print("Error in aplication start!\n");
		}
	}
}