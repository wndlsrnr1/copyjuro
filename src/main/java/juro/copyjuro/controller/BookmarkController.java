package juro.copyjuro.controller;

import juro.copyjuro.config.LoginUser;
import juro.copyjuro.config.model.ServiceUser;
import juro.copyjuro.controller.model.bookmark.BookmarkResponse;
import juro.copyjuro.controller.model.common.ApiResponse;
import juro.copyjuro.dto.bookmark.BookmarkDto;
import juro.copyjuro.service.BookmarkService;
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
