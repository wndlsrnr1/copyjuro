package juro.copyjuro.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import juro.copyjuro.config.auth.LoginUser;
import juro.copyjuro.config.auth.ServiceUser;
import juro.copyjuro.controller.common.ApiResponse;
import juro.copyjuro.controller.common.PageResponse;
import juro.copyjuro.controller.product.model.ProductCreateRequest;
import juro.copyjuro.controller.product.model.ProductResponse;
import juro.copyjuro.service.common.PageableDto;
import juro.copyjuro.service.product.model.ProductCreateRequestDto;
import juro.copyjuro.service.product.model.ProductDto;
import juro.copyjuro.service.product.model.ProductSearchRequestDto;
import juro.copyjuro.repository.product.model.ProductSortType;
import juro.copyjuro.repository.product.model.ProductStatus;
import juro.copyjuro.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products")
    public ApiResponse<PageResponse<ProductResponse>> searchProduct(
            @RequestParam(value = "size") @Min(1) Integer size,
            @RequestParam(required = false, value = "searchAfter") String searchAfter,
            @RequestParam(required = false, value = "productStatus") ProductStatus productStatus,
            @RequestParam(required = false, value = "userId") Long userId,
            @RequestParam(required = true, value = "sort") ProductSortType sort,
            @RequestParam(required = false, defaultValue = "false", value = "withTotalCount") boolean withTotalCount
    ) {
        ProductSearchRequestDto requestDto = ProductSearchRequestDto.builder()
                .productStatus(productStatus)
                .userId(userId)
                .size(size)
                .searchAfter(searchAfter)
                .sort(sort)
                .withTotalCount(withTotalCount)
                .build();

        PageableDto<ProductDto> dto = productService.searchProducts(requestDto);

        PageResponse<ProductResponse> response = PageResponse.<ProductResponse>builder()
                .items(dto.getItems().stream().map(ProductResponse::of).toList())
                .totalCount(dto.getTotalCount())
                .nextSearchAfter(dto.getNextSearchAfter())
                .build();

        return ApiResponse.success(response);
    }

    @GetMapping("/v1/products/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable(value = "id") Long id) {
        ProductDto productDto = productService.getProduct(id);
        ProductResponse response = ProductResponse.of(productDto);
        return ApiResponse.success(response);
    }

    @LoginUser
    @PostMapping("/v1/products")
    public ApiResponse<ProductResponse> createProduct(
            @AuthenticationPrincipal ServiceUser user,
            @RequestBody @Valid ProductCreateRequest request
    ) {

        ProductCreateRequestDto requestDto = request.toDto(user.getId());
        ProductDto product = productService.createProduct(requestDto);

        ProductResponse productResponse = ProductResponse.of(product);
        return ApiResponse.success(productResponse);
    }
}
