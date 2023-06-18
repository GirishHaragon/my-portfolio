package com.blog.repositories;

import com.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {//Instead of Ctrl+1 to import, in IntelliJ we use Alt+Enter
}
