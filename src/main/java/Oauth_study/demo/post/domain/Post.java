package Oauth_study.demo.post.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private Long view;

    private String title;

    //== 생성자 메소드==//
    public static Post create(String title){
        return Post.builder()
                .view(0L)
                .title(title)
                .build();
    }

    //== 조회수 증가 메소드==//
    public void upView(){
        this.view += 1L;
    }

    //== 조회수 설정 메소드==//
    public void viewSet(Long view){
        this.view = view;
    }
}
