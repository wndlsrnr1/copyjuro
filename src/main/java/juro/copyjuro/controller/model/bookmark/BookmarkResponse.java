package juro.copyjuro.controller.model.bookmark;

import juro.copyjuro.dto.bookmark.BookmarkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;

    public static BookmarkResponse of(BookmarkDto bookmarkDto) {
        return BookmarkResponse.builder()
                .id(bookmarkDto.getId())
                .userId(bookmarkDto.getUserId())
                .productId(bookmarkDto.getProductId())
                .createdAt(bookmarkDto.getCreatedAt())
                .build();
    }
}
