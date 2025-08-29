package com.ezdrive.ezdrive;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;
import com.ezdrive.ezdrive.rmi.RMIGameServiceImpl;
import com.ezdrive.ezdrive.services.QuestionService;

@SpringBootApplication
public class EzDriveApplication {

    public static void main(String[] args) {
        //
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        // activates Spring
        ApplicationContext context = SpringApplication.run(EzDriveApplication.class, args);

        try {
            // creates the RMI service with ApplicationContext
            RMIGameServiceImpl rmiService = new RMIGameServiceImpl(context);

            // creates registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // registers the service under the name "RMIGameService"
            registry.rebind("RMIGameService", rmiService);
        } catch (RemoteException e) {
            System.err.println("RMI service failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Bean
    ApplicationRunner seedQuestionsOnStartup(QuestionService questionService, QuestionRepository questionRepository) 
    {
        return args -> {
            if (questionRepository.count() == 0) {
                try (InputStream in = new ClassPathResource("theoryexamhe-data.xml").getInputStream()) {
                    questionService.importFromXmlStream(in);
                    System.out.println("Seeded questions from XML.");
                }
            }
        };
    }
}
