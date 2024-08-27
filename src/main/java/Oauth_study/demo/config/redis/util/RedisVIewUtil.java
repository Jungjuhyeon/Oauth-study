package Oauth_study.demo.config.redis.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisVIewUtil {
    private final RedisTemplate<String,Object> redisTemplate;

    public void addPostView(String key, String value, Long score) {
        redisTemplate.opsForZSet().add(key, value, score);
        // 키에 TTL 설정
    }
    public void plusPostView(String key, String value, Long score){
        redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    public void deletePost(String key){
        redisTemplate.delete(key);
    }

    //N개 가져오기
    public List<ZSetOperations.TypedTuple<Object>> getTopNValuesWithScore(String key, long start, long end) {
        // Redis에서 상위 N개의 value와 score를 함께 가져온다.
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);

        // Set을 List로 변환하여 반환 (필요에 따라 처리하기 편리하도록)
        return tuples != null ? new ArrayList<>(tuples) : Collections.emptyList();
    }



    public Double socre(String key,String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }
}
