package Oauth_study.demo.post.presentation;


import Oauth_study.demo.global.response.SuccessResponse;
import Oauth_study.demo.post.application.PostService;
import Oauth_study.demo.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/post/")
public class PostController {

    private final PostService postService;


    @GetMapping("/v1/view/{view_id}")
    public SuccessResponse<PostDto.Response.PostView> postView1(@PathVariable Long view_id){
        PostDto.Response.PostView response = postService.postView(view_id);
    return SuccessResponse.success(response);
    }

    @GetMapping("/v2/view/{view_id}")
    public SuccessResponse<PostDto.Response.PostView> postView2(@PathVariable Long view_id){
        PostDto.Response.PostView response = postService.postView2(view_id);
        return SuccessResponse.success(response);
    }

    @GetMapping("/v2/viewTop5")
    public SuccessResponse<List<PostDto.Response.PostTop5View>> postViewTop5(){
        List<PostDto.Response.PostTop5View> response = postService.postViewTop5();
        return SuccessResponse.success(response);
    }

    @PatchMapping("/v2/udpate")
    public SuccessResponse<String> postViewUpdate(){
        postService.postViewUpdate();
        return SuccessResponse.successWithoutResult("성공");
    }
}
