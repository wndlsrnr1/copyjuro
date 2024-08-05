package juro.copyjuro.service.bookmark;

import juro.copyjuro.service.bookmark.model.BookmarkDto;
import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.repository.bookmark.Bookmark;
import juro.copyjuro.repository.bookmark.BookmarkRepository;
import juro.copyjuro.service.product.ProductValidator;
import juro.copyjuro.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserValidator userValidator;
    private final ProductValidator productValidator;

    @Transactional
    public BookmarkDto registerBookmark(Long userId, Long productId) {
        userValidator.validateUserExists(userId);
        productValidator.validateProductExists(productId);

        bookmarkRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(bookmark -> {
                    throw new ClientException(ErrorCode.BAD_REQUEST, "already bookmarked. userId=%s, productId=%s".formatted(userId, productId));
                });

        Bookmark savedBookmark = bookmarkRepository.save(Bookmark.create(userId, productId));

        return BookmarkDto.of(savedBookmark);
    }

    /**
     * 입력 받아서 바로 삭제하는 것이 아닌 조회후 삭제
     */
    @Transactional
    public void removeBookmark(Long userId, Long productId) {
        userValidator.validateUserExists(userId);
        productValidator.validateProductExists(productId);

        Bookmark bookmark = bookmarkRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "bookmark not found. userId=%s, productId=%s".formatted(userId, productId)));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkDto> getBookmarks(Long userId) {
        userValidator.validateUserExists(userId);
        return bookmarkRepository.findByUserId(userId).stream().map(BookmarkDto::of).toList();
    }
}
