package juro.copyjuro.controller;

import static org.assertj.core.api.Assertions.*;

import juro.copyjuro.controller.model.common.PageResponse;
import juro.copyjuro.repository.product.model.Product;
import juro.copyjuro.repository.product.model.ProductStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import juro.copyjuro.config.DebuggingAuth;
import juro.copyjuro.controller.model.common.ApiResponse;
import juro.copyjuro.controller.model.product.ProductCreateRequest;
import juro.copyjuro.controller.model.product.ProductResponse;
import juro.copyjuro.repository.product.ProductRepository;
import juro.copyjuro.repository.user.UserRepository;

import java.util.List;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DebuggingAuth debuggingAuth;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private final static String LOCAL_HOST = "http://localhost:";

    public String getBaseUrl() {
        return LOCAL_HOST + port;
    }

    public String getUrl(String path) {
        return LOCAL_HOST + port + path;
    }

    @BeforeEach
    public void beforeEach() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("getProduct - procut 조회 성공")
    public void testGetProduct() throws Exception {
        //given
        Product product = Product.builder()
                .userId(1L)
                .name("testProduct")
                .price(1000L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build();

        Product savedProduct = productRepository.save(product);
        //when
        RestClient restClient = RestClient.create(getBaseUrl());

        ApiResponse<ProductResponse> response = restClient
                .get()
                .uri(getBaseUrl() + "/v1/products/{id}", savedProduct.getId())
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                });

        //then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getErrorCode()).isNull();
        assertThat(response.getBody().getId()).isEqualTo(savedProduct.getId());
    }

    @Test
    @DisplayName("createProduct - product 생성 성공")
    public void testCreateProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Test Product")
                .price(1000L)
                .quantity(10L)
                .build();

        //when
        RestClient restClient = RestClient.create(getBaseUrl());
        ApiResponse<ProductResponse> response = restClient
                .post()
                .uri(getUrl("/v1/products"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, debuggingAuth.getBearer())
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                })
                .getBody();


        //then
        assertThat(response).isNotNull();
        assertThat(response.getErrorCode()).isNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Product");
        assertThat(response.getBody().getPrice()).isEqualTo(1000L);
        assertThat(response.getBody().getQuantity()).isEqualTo(10L);

    }

    @Test
    @DisplayName("search - 전체 상품 4개 조건에 맞는 상품 3개 2개만 조회시 -> 조회 결과 2개, 전체 아이템수 3개, next 있음")
    public void testSearchProducts() throws Exception {
        // given
        Product product1 = Product.builder()
                .userId(1L)
                .name("testProduct")
                .price(1000L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build();

        Product product2 = Product.builder()
                .userId(1L)
                .name("testProduct")
                .price(1000L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build();
        Product product3 = Product.builder()
                .userId(1L)
                .name("testProduct")
                .price(1000L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build();

        Product product4 = Product.builder()
                .userId(2L)
                .name("testProduct")
                .price(1000L)
                .quantity(10L)
                .status(ProductStatus.AVAILABLE)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3, product4));

        String url = "/v1/products?size=2&productStatus=AVAILABLE&userId=1&sort=RECENT&withTotalCount=true";
        RestClient restClient = RestClient.create(getBaseUrl());

        // when
        ApiResponse<PageResponse<ProductResponse>> response =
                restClient.get()
                        .uri(url)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {
                        });


        // then
        assertThat(response).isNotNull();
        PageResponse<ProductResponse> body = response.getBody();

        assertThat(body.getTotalCount()).isEqualTo(3);
        assertThat(body.getItems()).size().isEqualTo(2);
        assertThat(body.getNextSearchAfter()).isNotNull();
    }


}
