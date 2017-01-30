package com.services;

import java.util.List;

/**
 * Created by Vadym on 29.01.2017.
 */
public interface TCPService {

    List getHosts();

    boolean verifyHosts();

    boolean runServers();

}
