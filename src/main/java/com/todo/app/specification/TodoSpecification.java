package com.todo.app.specification;

import com.todo.app.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class TodoSpecification implements Specification<Todo> {

  private final Todo filter;

  @Override
  public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    Predicate predicate = criteriaBuilder.disjunction();

    if(filter.getOwner() != null && filter.getOwner().getId() != null) {
      var owner = root.get("owner");
      var ownerId = owner.get("id");
      predicate.getExpressions().add(criteriaBuilder.equal(ownerId, filter.getOwner().getId()));
    }
    return predicate;
  }
}
