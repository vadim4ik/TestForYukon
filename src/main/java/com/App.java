package com;

import com.services.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
	@Autowired
	@Qualifier("applicationProperties")
	Properties applicationProperties;

	private class runServers implements Runnable {
            public void run() {
                    tcpService.runServers();
            }
    }

    private void start(String[] args) {
		String startTime = applicationProperties.getProperty("START_TIME");
		String endTime = applicationProperties.getProperty("END_TIME");
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String cal = parser.format(Calendar.getInstance().getTime());
        try {
            Date beginTime = parser.parse(startTime);
            Date finishTime = parser.parse(endTime);
            Date current = parser.parse(cal);

        	if (!(current.after(beginTime) && current.before(finishTime))) {
			Thread t = new Thread(new runServers());
			t.setPriority(10);
			t.start();
			try {
				while (true) {
					tcpService.verifyHosts();
				}
			}
			catch(Exception e) {
				System.out.print("Error in application start!\n");
			}
		}

        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
}