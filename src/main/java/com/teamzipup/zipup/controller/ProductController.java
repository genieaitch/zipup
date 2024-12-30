package com.teamzipup.zipup.controller;

import com.teamzipup.zipup.dto.Product;
import com.teamzipup.zipup.dto.User;
import com.teamzipup.zipup.service.ProductService;
import com.teamzipup.zipup.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @PostMapping("/product/add")
    public String productAdd(@RequestParam("productName") String productName,
                             @RequestParam("price") int price,
                             @RequestParam(value = "option1", required = false) String option1,
                             @RequestParam(value = "option2", required = false) String option2,
                             @RequestParam(value = "option3", required = false) String option3,
                             @RequestParam("category") String category,
                             @RequestParam("description") MultipartFile description,
                             @RequestParam("image") MultipartFile image,
                             HttpSession session,
                             Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        if (!"seller".equals(loginUser.getRole())) {
            model.addAttribute("error", "상품 등록 권한이 없습니다.");
            return "redirect:/";
        }

        long sellerId = loginUser.getId();

        try {
            // 상품 등록 후 생성된 ID 반환
            long productId = productService.insertProduct(sellerId, image, productName, price, option1, option2, option3, category, description);
            return "redirect:/product/detail/" + productId;
        } catch (Exception e) {
            model.addAttribute("error", "상품 등록 중 문제가 발생했습니다.");
            return "redirect:/product/add";
        }
    }


    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable("id") long id, Model model) {
        // 상품 조회
        Product product = productService.getProductById(id);
        if (product == null) {
            model.addAttribute("error", "상품을 찾을 수 없습니다.");
            return "redirect:/";
        }

        // 판매자 정보 조회
        User seller = userService.getUserById(product.getSellerId());
        if (seller == null) {
            model.addAttribute("error", "판매자 정보를 찾을 수 없습니다.");
            return "index";
        }

        // 옵션 처리
        List<String> option1List = product.getOption1() != null ? List.of(product.getOption1().split(",")) : List.of();
        List<String> option2List = product.getOption2() != null ? List.of(product.getOption2().split(",")) : List.of();

        model.addAttribute("product", product);
        model.addAttribute("companyName", seller.getCompanyName());
        model.addAttribute("option1List", option1List);
        model.addAttribute("option2List", option2List);
        model.addAttribute("description", product.getDescription());


        return "productDetail"; // 상세 페이지
    }
}



