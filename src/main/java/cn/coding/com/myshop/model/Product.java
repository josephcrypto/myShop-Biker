package cn.coding.com.myshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "product_tb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "brand", nullable = true, length = 50)
    private String brand;

    @Column(name = "year", nullable = true, length =50)
    private String year;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 5)
    private double price;

    @Lob
    @Column(name = "image", nullable = true, length = Integer.MAX_VALUE)
    private byte[] image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_Date", nullable = false)
    private Date createDate;


}
