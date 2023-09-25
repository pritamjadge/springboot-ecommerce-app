package com.project.springbootangularcrud.repository;

import com.project.springbootangularcrud.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
