package juro.copyjuro.repository.product;

import juro.copyjuro.repository.common.PageResult;
import juro.copyjuro.repository.product.model.Product;
import juro.copyjuro.repository.product.model.ProductSearchCriteria;

public interface CustomProductRepository {

    PageResult<Product> search(ProductSearchCriteria criteria);

}
