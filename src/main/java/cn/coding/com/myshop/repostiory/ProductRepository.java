package cn.coding.com.myshop.repostiory;

import cn.coding.com.myshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public Long countById(Long id);
}
