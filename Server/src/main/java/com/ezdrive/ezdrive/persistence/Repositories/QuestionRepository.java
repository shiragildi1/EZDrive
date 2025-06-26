package com.ezdrive.ezdrive.persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezdrive.ezdrive.persistence.Entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
