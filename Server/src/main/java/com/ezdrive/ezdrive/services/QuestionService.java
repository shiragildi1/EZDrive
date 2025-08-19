package com.ezdrive.ezdrive.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import com.ezdrive.ezdrive.api.dto.QuestionTriviaDto;
import com.ezdrive.ezdrive.persistence.Entities.Question;
import com.ezdrive.ezdrive.persistence.Repositories.QuestionRepository;

@Service
public class QuestionService 
{

    @Autowired
    private QuestionRepository questionRepository;

    // public void importFromXmlFile(String filePath) 
    // {
    //     List<Question> questions = extractQuestionsFromXml(filePath);
    //     questionRepository.saveAll(questions);
    // }
public void importFromXmlStream(InputStream inputStream) {
    List<Question> questions = extractQuestionsFromXml(inputStream);
    for (Question question : questions) {
        Question existing = questionRepository.findByQuestionText(question.getQuestionText());
        if (existing != null) {
            // עדכון ערכים קיימים
            existing.setCategory(question.getCategory());
            existing.setAnswer1(question.getAnswer1());
            existing.setAnswer2(question.getAnswer2());
            existing.setAnswer3(question.getAnswer3());
            existing.setAnswer4(question.getAnswer4());
            existing.setCorrectAnswer(question.getCorrectAnswer());
            existing.setImageUrl(question.getImageUrl());
            questionRepository.save(existing);
        } else {
            questionRepository.save(question);
        }
    }
}

    
    // private List<Question> extractQuestionsFromXml(String filePath) 
    // {
    //     List<Question> questions = new ArrayList<>();
    //     try 
    //     {
    //         File file = new File(filePath);
    //         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //         DocumentBuilder builder = factory.newDocumentBuilder();
    //         //Dom stractur
    //         org.w3c.dom.Document xmlDoc = builder.parse(file);

    //         NodeList items = xmlDoc.getElementsByTagName("item");
    //         for (int i = 0; i < items.getLength(); i++) 
    //         {
    //             org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);

    //             String title = getText(item, "title");          
    //             String description = getText(item, "description"); 
    //             String category = getText(item, "category"); 

    //             Question question = parseQuestionFromHtml(title, description, category);
    //             questions.add(question); 
                
    //         }
    //     } 
    //     catch (Exception e) 
    //     {
    //         e.printStackTrace(); 
    //     }
    //     return questions;
    // }
    private List<Question> extractQuestionsFromXml(InputStream inputStream) {
    List<Question> questions = new ArrayList<>();
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xmlDoc = builder.parse(inputStream);

        NodeList items = xmlDoc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);

            String title = getText(item, "title");
            String description = getText(item, "description");
            String category = getText(item, "category");

            Question question = parseQuestionFromHtml(title, description, category);
            if (question != null) {
                questions.add(question);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return questions;
}

    private String getText(org.w3c.dom.Element element, String tagName) 
    {
        NodeList list = element.getElementsByTagName(tagName);
        if (list.getLength() > 0)
        {
            return list.item(0).getTextContent().trim();
        }
        return null;
    }

    private Question parseQuestionFromHtml(String title, String html, String category) 
    {
        try {
            Document doc = Jsoup.parse(html);
            Elements liElements = doc.select("li");
            String[] answerTexts = new String[4];
            int correctAnswer = -1;
            String answerText = "";

            for (int i = 0; i < Math.min(liElements.size(), 4); i++) {
                Element li = liElements.get(i);
                Element span = li.selectFirst("span");

                if (span != null) 
                {
                    answerTexts[i] = span.text();

                    if (span.hasAttr("id") && span.attr("id").toLowerCase().startsWith("correctanswer")) 
                    {
                        correctAnswer = i + 1;
                    }
                }
            }
           Element img = doc.selectFirst("img");
            String imageUrl = (img != null) ? img.attr("src") : null;
            System.out.println("Image URL service: "+ imageUrl);

            if (correctAnswer == -1) return null;

            return new Question(
                null, title, category,
                answerTexts[0], answerTexts[1], answerTexts[2], answerTexts[3],
                correctAnswer, imageUrl
            );
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<QuestionTriviaDto> getQuestionsForGame(String category) {
    List<Question> questions = questionRepository.findByCategory(category);
    List<QuestionTriviaDto> dtoList = new ArrayList<>();

    for (Question q : questions) {
        dtoList.add(new QuestionTriviaDto(
            q.getQuestionId(),
            q.getQuestionText(),
            q.getCategory(),
            q.getAnswer1(),
            q.getAnswer2(),
            q.getAnswer3(),
            q.getAnswer4()
        ));
    }

    return dtoList;
}

    
}