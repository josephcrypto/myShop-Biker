package cn.coding.com.myshop.service;

import cn.coding.com.myshop.exception.ProductNotFoundException;
import cn.coding.com.myshop.model.Product;
import cn.coding.com.myshop.repostiory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> listAllProduct() {
        return (List<Product>) productRepository.findAll();
    }

//    public Product getProductById(Long id) throws ProductNotFoundException {
//        Optional<Product> result = productRepository.findById(id);
//        if (result.isPresent()) {
//            return result.get();
//        }
//        throw new ProductNotFoundException("Could not find any Product with ID  " + id);
//    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(Long id) throws ProductNotFoundException {
        Long count = productRepository.countById(id);
        if (count == null || count == 0 ) {
            throw new ProductNotFoundException("Could not find any Product with ID " + id);
        }
        productRepository.deleteById(id);
    }
}
