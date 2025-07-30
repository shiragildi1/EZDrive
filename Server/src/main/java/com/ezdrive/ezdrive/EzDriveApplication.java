package com.ezdrive.ezdrive;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ezdrive.ezdrive.rmi.RMIGameService;
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;
import com.ezdrive.ezdrive.services.MemoryGameService;

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

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000") 
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    };
}
@Bean(name = "rmiGameService")
public RMIGameServiceImpl rmiGameService(ApplicationContext context) throws RemoteException {
    return new RMIGameServiceImpl(context);
}

};



