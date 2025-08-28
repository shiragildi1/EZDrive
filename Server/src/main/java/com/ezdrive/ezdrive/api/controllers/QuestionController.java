package com.ezdrive.ezdrive.api.controllers;

import java.io.InputStream;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdrive.ezdrive.services.QuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController
{
    @Autowired
    private QuestionService questionService;

    @PostMapping("/xml")
    public ResponseEntity<?> importQuestionsFromXml()
    {
        try 
        {
            System.out.println("Checking questions");
            ClassPathResource resource = new ClassPathResource("theoryexamhe-data.xml");
            InputStream inputStream = resource.getInputStream();
            questionService.importFromXmlStream(inputStream);
           // File file = resource.getFile();
            System.out.println("Checking questions2");
           // questionService.importFromXmlFile(file.getAbsolutePath());
            return ResponseEntity.ok(Collections.singletonMap("message", "Succses!"));
        }
        catch (Exception e) 
        {
            System.out.println("Error importing questions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "The file not load" + e.getMessage()));
        }
    }
    

    @GetMapping("/category")
    public ResponseEntity<?> getQuestionsByCategory(@RequestParam String category) {
        try {
            return ResponseEntity.ok(questionService.getQuestionsForGame(category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error fetching game questions: " + e.getMessage()));
        }
    }
}
