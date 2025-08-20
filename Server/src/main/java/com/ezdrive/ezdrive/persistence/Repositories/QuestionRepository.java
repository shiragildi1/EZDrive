package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByQuestionText(String questionText);
    List<Question> findByCategory(String category);

    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Question> findRandom10ByCategoryForTrivia(@Param("category") String category);

    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM()LIMIT 12;", nativeQuery = true )
    List<Question> findRandom10ByCategoryForMemory(@Param("category") String category);

    @Query(value = "SELECT CASE correct_answer " +
                                                                    "WHEN 1 THEN answer1 " +
                                                                    "WHEN 2 THEN answer2 " +
                                                                    "WHEN 3 THEN answer3 " +
                                                                    "WHEN 4 THEN answer4 " +
                                                                    "END AS answerText " +
                "FROM questions WHERE question_id = :questionId ORDER BY RANDOM()LIMIT 10;", nativeQuery = true )
    String findCorrectAnswerByQuestion(@Param("questionId") Long question_id);
}
