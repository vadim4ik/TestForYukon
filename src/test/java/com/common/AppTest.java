package com.common;

        import com.App;
        import com.services.TCPService;
        import junit.framework.TestCase;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.mock.web.MockHttpServletResponse;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

        import java.math.BigInteger;
        import java.util.Arrays;
        import java.util.Collections;
        import java.util.Date;
        import java.util.List;

        import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/SpringBeans.xml")
public class AppTest extends TestCase {
    @Autowired
    TCPService tcpService;

    private class runServers implements Runnable {
        public void run() {
            tcpService.runServers();
        }
    }

    @Test
    public void testPortAvailable() {
        Thread t = new Thread(new AppTest.runServers());
        t.start();
        boolean available = tcpService.verifyHosts();

        assertNotNull(available);
        assertTrue(available);
        assertEquals(3, tcpService.getHosts().size());
    }

}
