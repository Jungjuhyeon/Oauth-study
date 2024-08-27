package Oauth_study.demo.post.dto;

import lombok.Builder;
import lombok.Getter;

public class PostDto {
    public static class Response {
        @Builder
        @Getter
        public static class PostView {
            private Long view;

            public static PostDto.Response.PostView of(Long view) {
                return PostDto.Response.PostView.builder()
                        .view(view)
//                        .tile(tile)
                        .build();
            }
        }

        @Builder
        @Getter
        public static class PostTop5View {
            private Long id;
            private Long view;
            private String title;

            public static PostDto.Response.PostTop5View of(Long id,Long view,String title) {
                return PostTop5View.builder()
                        .id(id)
                        .view(view)
                        .title(title)
                        .build();
            }
        }
    }
}
