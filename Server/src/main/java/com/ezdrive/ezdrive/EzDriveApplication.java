package com.ezdrive.ezdrive;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;

@SpringBootApplication
public class EzDriveApplication {

    public static void main(String[] args) {
        // מגדיר את כתובת ה־host ל־RMI
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        // מפעיל את Spring
        ApplicationContext context = SpringApplication.run(EzDriveApplication.class, args);

        try {
            // יוצר ידנית את שירות ה־RMI עם ApplicationContext
            RMIGameServiceImpl rmiService = new RMIGameServiceImpl(context);

            // יוצר registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // רושם את השירות תחת השם "RMIGameService"
            registry.rebind("RMIGameService", rmiService);
        } catch (RemoteException e) {
            System.err.println("RMI service failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
