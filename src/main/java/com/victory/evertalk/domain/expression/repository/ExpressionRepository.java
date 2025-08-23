package com.victory.evertalk.domain.expression.repository;

import com.victory.evertalk.domain.episode.entity.Chat;
import com.victory.evertalk.domain.expression.entity.Expression;
import com.victory.evertalk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpressionRepository extends JpaRepository<Expression, Integer> {
    List<Expression> findByUserAndChat(User user, Chat chat);
}
