package juro.copyjuro.service;

import juro.copyjuro.service.bookmark.model.BookmarkDto;
import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.repository.bookmark.Bookmark;
import juro.copyjuro.repository.bookmark.BookmarkRepository;
import juro.copyjuro.service.bookmark.BookmarkService;
import juro.copyjuro.service.product.ProductValidator;
import juro.copyjuro.service.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 개발 시 Exception이 발생하는 상황에 대한 결과 값 예측
 * 분기 별로 예상 됐을 때 처리 예측
 * 상황 설정 -> 실행 -> 결과값 예측
 */
@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {BookmarkService.class})
public class BookmarkServiceTest {

    @MockBean
    private BookmarkRepository bookmarkRepository;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private ProductValidator productValidator;

    @Autowired
    private BookmarkService bookmarkService;

    private Long userId;
    private Long productId;
    private Bookmark bookmark;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        productId = 1L;
        bookmark = Bookmark.builder()
                .userId(userId)
                .productId(productId)
                .build();
    }

    @Test
    void testRegisterBookmark_Success() {
        when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

        BookmarkDto result = bookmarkService.registerBookmark(userId, productId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getProductId()).isEqualTo(productId);

        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(1)).validateProductExists(productId);
        verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    void testRegisterBookmark_UserNotFound() {
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=".formatted(userId)))
                .when(userValidator).validateUserExists(userId);

        ClientException exception = assertThrows(ClientException.class, () -> bookmarkService.registerBookmark(userId, productId));

        assertThat(exception.getMessage()).contains("user not found. userId=".formatted(userId));

        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(0)).validateProductExists(productId);
        verify(bookmarkRepository, times(0)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
    }

    @Test
    void testRegisterBookmark_ProductNotFound() {
        //doNothing().when(userValidator).validateUserExists(userId); //검증 안함, 없어도 같은데 확실히 하기 위해서?
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "product not found. productId=" + productId))
                .when(productValidator).validateProductExists(productId);

        ClientException exception = assertThrows(ClientException.class,
                () -> bookmarkService.removeBookmark(userId, productId)
        );

        assertThat(exception.getMessage()).contains("product not found");
        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(1)).validateProductExists(productId);
        verify(bookmarkRepository, times(0)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
    }

    @Test
    void testRegisterBookmark_AlreadyBookmarked() {
        when(bookmarkRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(Optional.of(bookmark));

        ClientException exception = assertThrows(ClientException.class, () -> bookmarkService.registerBookmark(userId, productId));

        assertThat(exception.getMessage()).contains("already bookmarked");

        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(1)).validateProductExists(productId);
        verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
    }

    @Test
    void testRemoveBookmark_Success() {
        when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(bookmark));

        bookmarkService.removeBookmark(userId, productId);

        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(1)).validateProductExists(productId);
        verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(1)).delete(bookmark);
    }

    @Test
    void testRemoveBookmark_NotFound() {
        when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());

        ClientException exception = assertThrows(ClientException.class, () ->
                bookmarkService.removeBookmark(userId, productId)
        );
        assertThat(exception.getMessage()).contains("bookmark not found");
        verify(userValidator, times(1)).validateUserExists(userId);
        verify(productValidator, times(1)).validateProductExists(productId);
        verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
        verify(bookmarkRepository, times(0)).delete(any(Bookmark.class)); //bookmark가 아니라 임의의 Bookmark Class 에 대해서 검증
    }

    @Test
    void testGetBookmarks_Success() {
        when(bookmarkRepository.findByUserId(userId)).thenReturn(Collections.singletonList(bookmark));

        List<BookmarkDto> bookmarks = bookmarkService.getBookmarks(userId);

        assertThat(bookmarks).hasSize(1);
        assertThat(bookmarks.get(0).getUserId()).isEqualTo(userId);
        verify(userValidator, times(1)).validateUserExists(userId);
        verify(bookmarkRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetBookmarks_UserNotFound() {
        doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId))
                .when(userValidator).validateUserExists(userId);

        ClientException exception = assertThrows(ClientException.class, () -> bookmarkService.getBookmarks(userId));

        assertThat(exception.getMessage()).contains("user not found");
        verify(userValidator, times(1)).validateUserExists(userId);
        verify(bookmarkRepository, times(0)).findByUserId(userId);
    }

}
