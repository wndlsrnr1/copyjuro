package juro.copyjuro.repository.product;

import juro.copyjuro.repository.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
}
