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
import ru.vladbstrv.catalogueservice.controller.payload.UpdateProductPayload;
import ru.vladbstrv.catalogueservice.entity.Product;
import ru.vladbstrv.catalogueservice.service.ProductService;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(
            @PathVariable("productId") int productId,
            @Valid @RequestBody UpdateProductPayload payload,
            BindingResult bindingResult,
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
            return ResponseEntity.badRequest().body(problemDetail);
        }else {
            this.productService.updateProduct(productId, payload.title(), payload.details());
            return ResponseEntity.noContent().build();
        }
    }
}
