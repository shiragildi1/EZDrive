package com.ezdrive.ezdrive;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ezdrive.ezdrive.rmi.RMIGameService;
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;

@SpringBootApplication
public class EzDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzDriveApplication.class, args);
        try 
        {
            RMIGameService service = new RMIGameServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            // registry.bind("RMIGameService", service); //return Exceptioon if user already exist.
            registry.rebind("RMIGameService", service);
            System.out.println("GameService is live");
            System.out.println("RMI service is running...");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}

    @Bean(name = "rmiGameService")
    public RMIGameServiceImpl rmiGameService(ApplicationContext context) throws RemoteException {
        return new RMIGameServiceImpl(context);
    }
};



