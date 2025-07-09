package com.ezdrive.ezdrive.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
}
