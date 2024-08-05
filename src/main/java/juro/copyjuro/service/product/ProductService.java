package juro.copyjuro.service.product;

import juro.copyjuro.service.common.PageableDto;
import juro.copyjuro.service.product.model.ProductCreateRequestDto;
import juro.copyjuro.service.product.model.ProductDto;
import juro.copyjuro.service.product.model.ProductSearchRequestDto;
import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.repository.common.PageResult;
import juro.copyjuro.repository.product.ProductRepository;
import juro.copyjuro.repository.product.model.Product;
import juro.copyjuro.repository.product.model.ProductSearchCriteria;
import juro.copyjuro.repository.product.model.ProductStatus;
import juro.copyjuro.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserValidator userValidator;

    @Transactional
    public ProductDto createProduct(ProductCreateRequestDto requestDto) {
        userValidator.validateUserExists(requestDto.getUserId());
        Product product = Product.builder()
                .userId(requestDto.getUserId())
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .status(ProductStatus.AVAILABLE)
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductDto.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ClientException(ErrorCode.BAD_REQUEST, "there is not product. productId = %s".formatted(id))
                );

        return ProductDto.of(product);
    }


    @Transactional(readOnly = true)
    public PageableDto<ProductDto> searchProducts(ProductSearchRequestDto requestDto) {
        ProductSearchCriteria criteria = requestDto.toCriteria();

        PageResult<Product> search = productRepository.search(criteria);

        return PageableDto.<ProductDto>builder()
                .items(search.getItems().stream().map(ProductDto::of).toList())
                .totalCount(search.getTotalCount())
                .nextSearchAfter(search.getNextSearchAfter())
                .build();
    }
}
