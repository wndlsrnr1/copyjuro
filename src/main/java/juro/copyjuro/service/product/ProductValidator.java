package juro.copyjuro.service.product;

import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository productRepository;

    /**
     * 왜 validate를 따로 썼는지? -> todo: 장점 적기
     */
    public void validateProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "product not found. productId=%s".formatted(productId));
        }
    }
}
