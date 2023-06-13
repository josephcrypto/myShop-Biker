package cn.coding.com.myshop.web;

import cn.coding.com.myshop.exception.ProductNotFoundException;
import cn.coding.com.myshop.model.Product;
import cn.coding.com.myshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${uploadDir}")
    private String uploadFolder;

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping(value = {"/", "/home"})
    public String addProduct() {
        return "home";
    }

    @GetMapping("/bike/form")
    public String showForm() {
        return "form";
    }

    @PostMapping("/bike/saveBikeDetails")
    @ResponseBody
    public ResponseEntity<?> createProduct(@RequestParam("name") String name, @RequestParam("brand") String brand,
                                           @RequestParam("year") String year, @RequestParam("description") String description, @RequestParam("status") String status,
                                           Model model, HttpServletRequest request,
                                           @RequestParam("price") double price, final @RequestParam("image") MultipartFile file) {
        try {
            //String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            logger.info("UploadDirectory====> " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            logger.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Can't upload! File name contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! File name contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] names = name.split(",");
            String[] brands = brand.split(",");
            String[] years = year.split("-");
            String[] descriptions = description.split(",");
            String[] condition = status.split(",");
            Date createDate = new Date();
            logger.info("Name: " + names[0]+ " " + filePath );
            logger.info("brand: " + brands[0]);
            logger.info("year: " + years[0]);
            logger.info("description: " + descriptions[0]);
            logger.info("price: " + price);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    logger.info("Folder Created");
                    dir.mkdirs();
                }
                //Save file Local
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception exc) {
                logger.info("in catch");
                exc.printStackTrace();
            }
            byte[] imageDate = file.getBytes();
            Product product = new Product();
            product.setName(names[0]);
            product.setBrand(brands[0]);
            product.setYear(years[0]);
            product.setImage(imageDate);
            product.setDescription(descriptions[0]);
            product.setPrice(price);
            product.setCreateDate(createDate);
            product.setStatus(status);
            productService.saveProduct(product);
            logger.info("HttpStatus ======> " + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bike/display/{id}")
    @ResponseBody
    public void showProduct(@PathVariable("id") Long id, HttpServletResponse response, Optional<Product> product)
            throws ServletException, IOException {
        logger.info("Bike Id  :: " + id);
        product = productService.getProductById(id);
        response.setContentType("image/jpeg");
        response.setContentType("image/jpg" );
        response.setContentType("image/png");
        response.setContentType("image/gif");
        response.getOutputStream().write(product.get().getImage());
        response.getOutputStream().close();

    }

    @GetMapping("/bike/bikeDetails")
    public String showBikeDetails(@RequestParam("id") Long id, Optional<Product> product, Model model) {
        try {
            logger.info("Biker ID:: " + id);
            if (id != 0) {
                product = productService.getProductById(id);
                logger.info("bikers :: " + product);
                if (product.isPresent()) {
                    model.addAttribute("id", product.get().getId());
                    model.addAttribute("name", product.get().getName());
                    model.addAttribute("brand", product.get().getBrand());
                    model.addAttribute("year", product.get().getYear());
                    model.addAttribute("description", product.get().getDescription());
                    model.addAttribute("price", product.get().getPrice());
                    model.addAttribute("status", product.get().getStatus());
                    return "bikedetails";
                }
                return "redirect:/home";
            }
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/bike/list")
    public String show(Model map) {
        List<Product> products = productService.listAllProduct();
        map.addAttribute("products", products);
        return "bike-list";
    }

    @GetMapping("/bike/new")
    public String showNewBike(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add New Bike");
        return "form";
    }

    @GetMapping("/bike/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        try {
            Product product = productService.getProductById(id).get();
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Bike (ID: " + id + ")");
            redirect.addFlashAttribute("message", "Bike is Updated.");
            return "form";
        }catch (Exception exc) {
            redirect.addFlashAttribute("message", exc.getMessage());
            return "redirect:/bike/list";
        }
    }

    @GetMapping("/bike/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            productService.deleteProductById(id);
            redirect.addFlashAttribute("message", "Product with ID " + id + " has been deleted!");
        } catch (ProductNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/home";
    }
}
