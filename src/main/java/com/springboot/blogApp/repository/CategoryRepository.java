package com.springboot.blogApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blogApp.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
