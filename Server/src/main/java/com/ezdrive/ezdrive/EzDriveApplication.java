package com.ezdrive.ezdrive;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ezdrive.ezdrive.rmi.RMIGameService;
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;

@SpringBootApplication
public class EzDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzDriveApplication.class, args);
       try {
            RMIGameService stub = new RMIGameServiceImpl();
            LocateRegistry.createRegistry(1099).rebind("GameService", stub);
            System.out.println("GameService is live");
        } catch (RemoteException e) {
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


}
