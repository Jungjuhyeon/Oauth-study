package Oauth_study.demo.post.application;


import Oauth_study.demo.config.redis.DistributedLock;
import Oauth_study.demo.config.redis.util.RedisVIewUtil;
import Oauth_study.demo.post.domain.Post;
import Oauth_study.demo.post.infrastructure.PostJpaRepository;
import Oauth_study.demo.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final RedisVIewUtil redisVIewUtil;
    private static final String VIEW_Id = "viewId";
    private static final String VIEW = "view";


    @Transactional
    public PostDto.Response.PostView postView(Long viewId) {
        Post post = postJpaRepository.findById(viewId).orElseThrow();
        post.upView();
        return PostDto.Response.PostView.of(post.getView());
    }

    @DistributedLock(value = "view:#viewId")
    public PostDto.Response.PostView postView2(Long viewId) {

        // Redis에서 조회수 가져오기
        Double check = redisVIewUtil.socre(VIEW, VIEW_Id + viewId);

        if (check == null) {
            // 분산 락을 걸고 DB 조회 및 Redis 저장을 처리
            return handleViewWithLock(viewId);

        }

        // Redis 조회수 증가
        redisVIewUtil.plusPostView(VIEW, VIEW_Id + viewId, 1L);

        // 증가된 조회수 반환
        return PostDto.Response.PostView.of(check.longValue() + 1);
    }

    @Transactional
    public PostDto.Response.PostView handleViewWithLock(Long viewId) {
        // DB에서 게시글 조회
        Post post = postJpaRepository.findById(viewId).orElseThrow();
        Long currentView = post.getView(); // 현재 조회수 가져오기

        // Redis에 조회수 저장
        redisVIewUtil.addPostView(VIEW, VIEW_Id + viewId, currentView + 1);

        // 업데이트된 조회수 반환
        return PostDto.Response.PostView.of(currentView + 1);
    }

    public List<PostDto.Response.PostTop5View> postViewTop5() {
        // 1. Redis에서 상위 5개의 value와 score 가져오기
        List<ZSetOperations.TypedTuple<Object>> topNValuesWithScores = redisVIewUtil.getTopNValuesWithScore(VIEW, 0, 4);

        // 2. 데이터베이스에서 상세 정보 조회
        List<PostDto.Response.PostTop5View> postTop5Views = topNValuesWithScores.stream()
                .map(tuple -> {
                    String value = (String) tuple.getValue();
                    Long score = tuple.getScore().longValue();
                    Long viewId = Long.parseLong(value.substring("viewId".length()));

                    Post post = postJpaRepository.findById(viewId).orElseThrow();
                    return PostDto.Response.PostTop5View.of(post.getId(), score, post.getTitle()); // DTO 변환
                })
                .toList();

        return postTop5Views;
    }

    public List<PostDto.Response.PostTop5View> postViewTop5_1(){
        List<Post> postList = postJpaRepository.findTop10ByOrderByViewDesc();

        List<PostDto.Response.PostTop5View> postTop10Views = postList.stream()
                .map(o -> {
                    return PostDto.Response.PostTop5View.of(o.getId(), o.getView(), o.getTitle()); // DTO 변환
                })
                .toList();

        return postTop10Views;
    }



    @Transactional
    public void postViewUpdate() {
        // 1. Redis에서 모든 value와 score 가져오기
        List<ZSetOperations.TypedTuple<Object>> topNValuesWithScores = redisVIewUtil.getTopNValuesWithScore(VIEW, 0, -1);

        // 2. 데이터베이스에 저장
        for (ZSetOperations.TypedTuple<Object> tuple : topNValuesWithScores) {
            String value = (String) tuple.getValue();
            Long score = tuple.getScore().longValue();

            Long viewId = Long.parseLong(value.substring("viewId".length()));

            Post post = postJpaRepository.findById(viewId).orElseThrow();

            post.viewSet(score); //조회수 설정
        }
//        // 2. 한 번의 트랜잭션에서 배치 업데이트 처리
//        Map<Long, Long> viewUpdates = new HashMap<>();
//
//        for (ZSetOperations.TypedTuple<Object> tuple : topNValuesWithScores) {
//            String value = (String) tuple.getValue();
//            Long score = tuple.getScore().longValue();
//            Long viewId = Long.parseLong(value.substring("viewId".length()));
//
//            viewUpdates.put(viewId, score);
//        }
//
//        // 배치 업데이트 실행
//        for (Map.Entry<Long, Long> entry : viewUpdates.entrySet()) {
//            postJpaRepository.updateView(entry.getKey(), entry.getValue());
//        }

        //view에 있는 값 삭제
        redisVIewUtil.deletePost(VIEW);

        //디비에서 상위 10개 가져오기
        List<Post> postList = postJpaRepository.findTop10ByOrderByViewDesc();


        // Redis에 조회수 저장
        for(Post post : postList){
            redisVIewUtil.addPostView(VIEW, VIEW_Id + post.getId(), post.getView());
        }

    }
}



//        for(int i=1;i<31;i++){
//            String title = "주현"+i;
//            Post post = Post.create(title);
//            postJpaRepository.save(post);
//        }