package ru.vladbstrv.catalogueservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vladbstrv.catalogueservice.controller.payload.NewProductPayload;
import ru.vladbstrv.catalogueservice.entity.Product;
import ru.vladbstrv.catalogueservice.service.ProductService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @GetMapping
    public List<Product> findProducts() {
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody NewProductPayload payload,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder,
            Locale locale
    ) {
        if (bindingResult.hasErrors()) {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(
                            HttpStatus.BAD_REQUEST,
                            this.messageSource.getMessage(
                                    "errors.400.title",
                                    new Object[0],
                                    "errors.400.title",
                                    locale
                            )
                    );
            problemDetail.setProperty("errors",
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .toList()
            );
            return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}
