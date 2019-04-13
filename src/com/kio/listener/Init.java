package com.kio.listener;

import com.kio.entity.SystemParameters;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

@WebListener
public class Init implements ServletContextListener {

	public static SystemParameters PARAMETERS;
    public Init() {
        try {
            PARAMETERS = SystemParameters.loadFromFile();
            System.out.println("Load Parameter finish!");
        }catch (IOException e){
            System.err.println("Load Parameter failed!");
            e.printStackTrace();
        }

    }

    public void contextDestroyed(ServletContextEvent arg0)  {
        try {
            PARAMETERS.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void contextInitialized(ServletContextEvent arg0)  {

    }
	
}
