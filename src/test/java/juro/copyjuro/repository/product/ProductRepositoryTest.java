package juro.copyjuro.repository.product;

import juro.copyjuro.repository.common.PageResult;
import juro.copyjuro.repository.product.model.Product;
import juro.copyjuro.repository.product.model.ProductSearchCriteria;
import juro.copyjuro.repository.product.model.ProductSortType;
import juro.copyjuro.repository.product.model.ProductStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CustomProductRepositoryImpl.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        // Insert test data
        productRepository.save(Product.builder()
                .userId(1L)
                .name("Product 1")
                .price(100L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build());
        productRepository.save(Product.builder()
                .userId(2L)
                .name("Product 2")
                .price(200L)
                .quantity(20L)
                .status(ProductStatus.SOLD_OUT)
                .build());
        productRepository.save(Product.builder()
                .userId(1L)
                .name("Product 3")
                .price(300L)
                .quantity(30L)
                .status(ProductStatus.PAUSED)
                .build());
        productRepository.save(Product.builder()
                .userId(2L)
                .name("Product 4")
                .price(400L)
                .quantity(40L)
                .status(ProductStatus.DELETED)
                .build());
        productRepository.save(Product.builder()
                .userId(1L)
                .name("Product 5")
                .price(150L)
                .quantity(15L)
                .status(ProductStatus.AVAILABLE)
                .build());
    }

   @Test
   @DisplayName("search - 삭제된 상품 조회")
   public void testSearchDeleteProducts() throws Exception {
       //given
       ProductSearchCriteria criteria = ProductSearchCriteria.builder()
               .withTotalCount(true)
               .productStatus(ProductStatus.DELETED)
               .size(10)
               .sort(ProductSortType.RECENT)
               .build();

       //when
       PageResult<Product> result = productRepository.search(criteria);

       //then
       Assertions.assertThat(result.getItems()).hasSize(1);
       Assertions.assertThat(result.getTotalCount()).isEqualTo(1);
       Assertions.assertThat(result.getItems().get(0).getName()).isEqualTo("Product 4");
       Assertions.assertThat(result.getNextSearchAfter()).isNull();
   }

   @Test
   @DisplayName("search - 최신 순 조회")
   public void testSearchWithPaginationAndSorting() throws Exception {
       //given
       ProductSearchCriteria criteria = ProductSearchCriteria.builder()
               .productStatus(ProductStatus.AVAILABLE)
               .size(1)
               .sort(ProductSortType.RECENT)
               .withTotalCount(true)
               .build();

       // Fetch first page

       PageResult<Product> result = productRepository.search(criteria);

       Assertions.assertThat(result.getItems()).hasSize(1);
       Assertions.assertThat(result.getTotalCount()).isEqualTo(2);
       Assertions.assertThat(result.getItems().get(0).getName()).isEqualTo("Product 5");
       Assertions.assertThat(result.getNextSearchAfter()).isNotNull();

       // Fetch the next page

       criteria.setSearchAfter(result.getNextSearchAfter());
       result = productRepository.search(criteria);

       Assertions.assertThat(result.getItems()).hasSize(1);
       Assertions.assertThat(result.getItems().get(0).getName()).isEqualTo("Product 1");
       Assertions.assertThat(result.getNextSearchAfter()).isNotNull();

       // Fetch the next page
       criteria.setSearchAfter(result.getNextSearchAfter());
       result = productRepository.search(criteria);
       Assertions.assertThat(result.getItems()).hasSize(0);
       Assertions.assertThat(result.getNextSearchAfter()).isNull();
   }

   @Test
   @DisplayName("가격순 조회")
   public void testSearchSortedByPriceWithPagination() throws Exception {
       ProductSearchCriteria criteria = ProductSearchCriteria.builder()
               .productStatus(ProductStatus.AVAILABLE)
               .size(1)
               .sort(ProductSortType.PRICE)
               .withTotalCount(true)
               .build();

       // First page
       PageResult<Product> result = productRepository.search(criteria);

       assertThat(result.getItems()).hasSize(1);
       assertThat(result.getTotalCount()).isEqualTo(2); // Assuming there are 2 available products
       assertThat(result.getItems().get(0).getName()).isEqualTo("Product 5"); // Cheapest product
       assertThat(result.getNextSearchAfter()).isNotNull();

       // Second page
       criteria.setSearchAfter(result.getNextSearchAfter());
       result = productRepository.search(criteria);

       assertThat(result.getItems()).hasSize(1);
       assertThat(result.getItems().get(0).getName()).isEqualTo("Product 1"); // Next cheapest product
       assertThat(result.getNextSearchAfter()).isNotNull(); // No more records after this page

       // third page
       criteria.setSearchAfter(result.getNextSearchAfter());
       result = productRepository.search(criteria);

       assertThat(result.getItems()).hasSize(0);
       assertThat(result.getNextSearchAfter()).isNull(); // No more records after this page
   }
}
