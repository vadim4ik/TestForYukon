package com.services;

import java.util.List;
import java.util.Set;

/**
 * Created by Vadym on 29.01.2017.
 */
public interface TCPService {

    Set getHosts();

    boolean verifyHosts() throws InterruptedException;

    boolean runServers();

}
