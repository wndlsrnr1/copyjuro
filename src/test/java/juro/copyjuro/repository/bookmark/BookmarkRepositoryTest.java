package juro.copyjuro.repository.bookmark;

import juro.copyjuro.config.jpa.JpaConfig;
import juro.copyjuro.repository.product.ProductRepository;
import juro.copyjuro.repository.product.model.Product;
import juro.copyjuro.repository.user.UserRepository;
import juro.copyjuro.repository.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //load entity, and jpa repositories
@ActiveProfiles("local")
@Import(JpaConfig.class) //load additional configuration
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private User user;
    private Product product;

    /**
     * 기존의 연관관계에서 만들어지는 table이므로 공통적으로 필요한 기본 데이터 세팅 해줌.
     */
    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("testUser")
                .password("password")
                .build();

        user = userRepository.save(user);

        product = Product.builder()
                .userId(user.getId())
                .name("Test Product")
                .price(100L)
                .quantity(100L)
                .build();

        product = productRepository.save(product);
    }

    @Test
    void testAddBookmark() {
        Bookmark bookmark = Bookmark.builder()
                .userId(user.getId())
                .productId(product.getId())
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        assertThat(savedBookmark.getId()).isNotNull();
        assertThat(savedBookmark.getUserId()).isEqualTo(user.getId());
        assertThat(savedBookmark.getProductId()).isEqualTo(product.getId());
        assertThat(savedBookmark.getCreatedAt()).isNotNull();
    }

    @Test
    void testFindByUser() {
        Bookmark bookmark = Bookmark.builder()
                .userId(user.getId())
                .productId(product.getId())
                .build();
        bookmarkRepository.save(bookmark);

        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(user.getId());
        assertThat(bookmarks).hasSize(1);
        assertThat(bookmarks.get(0).getProductId()).isEqualTo(product.getId());
    }

    @Test
    void testFindByUserAndProduct() {
        Bookmark bookmark = Bookmark.builder()
                .userId(user.getId())
                .productId(product.getId())
                .build();
        bookmarkRepository.save(bookmark);

        Optional<Bookmark> foundBookmark = bookmarkRepository.findByUserIdAndProductId(user.getId(), product.getId());
        assertThat(foundBookmark).isPresent();
        assertThat(foundBookmark.get().getUserId()).isEqualTo(user.getId());
        assertThat(foundBookmark.get().getProductId()).isEqualTo(product.getId());
    }

    @Test
    void testRemoveBookmark() {
        Bookmark bookmark = Bookmark.create(user.getId(), product.getId());
        bookmark = bookmarkRepository.save(bookmark);
        bookmarkRepository.delete(bookmark);
        Optional<Bookmark> foundBookmark = bookmarkRepository.findById(bookmark.getId());
        assertThat(foundBookmark).isNotPresent();
    }
}
