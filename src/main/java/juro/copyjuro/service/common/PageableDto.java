package juro.copyjuro.service.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto<T> {
    private List<T> items;
    //더 이상 조회할 아이템이 없을 경우 null
    private String nextSearchAfter;
    //전체 아이템 수
    private Long totalCount;
}
