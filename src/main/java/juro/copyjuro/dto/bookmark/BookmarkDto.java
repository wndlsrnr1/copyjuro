package juro.copyjuro.dto.bookmark;

import juro.copyjuro.repository.bookmark.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;

    /**
     * 생성자 역할을 하는 static method, bookmark에서 만드는 것이 아닌 bookmarkDto에서 만들어야 domain이 dto에 대해서 의존성을 가지지 않음.
     */
    public static BookmarkDto of(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUserId())
                .productId(bookmark.getProductId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
