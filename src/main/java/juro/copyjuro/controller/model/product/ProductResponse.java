package juro.copyjuro.controller.model.product;

import juro.copyjuro.dto.product.ProductDto;
import juro.copyjuro.repository.product.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private Long userId;
    private String name;
    private Long price;
    private Long quantity;
    private ProductStatus status;

    public static ProductResponse of(ProductDto productDto) {
        return ProductResponse.builder()
                .id(productDto.getId())
                .userId(productDto.getUserId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .status(productDto.getStatus())
                .build();
    }
}
