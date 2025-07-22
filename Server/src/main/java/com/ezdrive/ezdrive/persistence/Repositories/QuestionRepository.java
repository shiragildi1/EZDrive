package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);

    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Question> findRandom10ByCategoryForTrivia(@Param("category") String category);
}
