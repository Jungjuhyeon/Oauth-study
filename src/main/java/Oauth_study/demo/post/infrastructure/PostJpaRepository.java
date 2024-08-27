package Oauth_study.demo.post.infrastructure;

import Oauth_study.demo.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post,Long> {

    // 조회수를 기준으로 상위 10개 포스트를 가져오는 쿼리
    List<Post> findTop10ByOrderByViewDesc();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.view = :view WHERE p.id = :id")
    void updateView(Long id, Long view);

}