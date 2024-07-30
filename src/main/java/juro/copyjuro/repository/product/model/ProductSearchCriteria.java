package juro.copyjuro.repository.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchCriteria {
    private Long userId; //어떤 유저

    private ProductStatus productStatus; //상태
    private ProductSortType sort; //어떤 정렬 방식인지

    private Integer size; //한페이지당 아이템 수

    private String searchAfter; //마지막 검색 이름
    private boolean withTotalCount; //전 검색에서의 최대 검색 수가 필요한지?
}
