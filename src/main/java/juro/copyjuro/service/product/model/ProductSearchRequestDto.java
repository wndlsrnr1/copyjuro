package juro.copyjuro.service.product.model;

import juro.copyjuro.repository.product.model.ProductSearchCriteria;
import juro.copyjuro.repository.product.model.ProductSortType;
import juro.copyjuro.repository.product.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequestDto {
    private Long userId;

    private ProductStatus productStatus;
    private ProductSortType sort;

    private Integer size;

    private String searchAfter;
    private boolean withTotalCount;

    public ProductSearchCriteria toCriteria() {
        return ProductSearchCriteria.builder()
                .userId(userId)
                .productStatus(productStatus)
                .sort(sort)
                .size(size)
                .searchAfter(searchAfter)
                .withTotalCount(withTotalCount)
                .build();
    }
}
