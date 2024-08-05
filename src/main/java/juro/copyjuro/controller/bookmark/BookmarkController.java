package juro.copyjuro.controller.bookmark;

import juro.copyjuro.config.auth.LoginUser;
import juro.copyjuro.config.auth.ServiceUser;
import juro.copyjuro.controller.bookmark.model.BookmarkResponse;
import juro.copyjuro.controller.common.ApiResponse;
import juro.copyjuro.service.bookmark.model.BookmarkDto;
import juro.copyjuro.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @LoginUser
    @GetMapping("/v1/bookmarks")
    public ApiResponse<List<BookmarkResponse>> getBookmarks(
            @AuthenticationPrincipal ServiceUser serviceUser
    ) {
        List<BookmarkResponse> response = bookmarkService.getBookmarks(serviceUser.getId())
                .stream()
                .map(BookmarkResponse::of)
                .toList();
        return ApiResponse.success(response);
    }

    @LoginUser
    @PostMapping("/v1/products/{productId}/bookmarks")
    public ApiResponse<BookmarkResponse> addBookmark(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "productId") Long productId
    ) {
        BookmarkDto bookmarkDto = bookmarkService.registerBookmark(serviceUser.getId(), productId);
        BookmarkResponse response = BookmarkResponse.of(bookmarkDto);
        return ApiResponse.success(response);
    }

    @LoginUser
    @DeleteMapping("/v1/products/{productId}/bookmarks")
    public ApiResponse<Void> removeBookmark (
            @AuthenticationPrincipal ServiceUser serviceUser,
            @PathVariable(value = "productId") Long productId
    ) {
        bookmarkService.removeBookmark(serviceUser.getId(), productId);
        return ApiResponse.success();
    }



}
