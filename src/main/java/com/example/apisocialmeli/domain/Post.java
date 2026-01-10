package com.example.apisocialmeli.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Embedded
    private Product product;

    @Column(nullable = false)
    private int category;

    @Column(nullable = false)
    private double price;

    @Column(name = "has_promo", nullable = false)
    private boolean hasPromo;

    private Double discount;

    protected Post() {
    }

    public Post(int userId,
                LocalDate date,
                Product product,
                int category,
                double price,
                boolean hasPromo,
                Double discount) {
        this.user = new User(userId);
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
        this.hasPromo = hasPromo;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user.getId();
    }

    public LocalDate getDate() {
        return date;
    }

    public Product getProduct() {
        return product;
    }

    public int getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isHasPromo() {
        return hasPromo;
    }

    public Double getDiscount() {
        return discount;
    }

    public User getUser() {
        return user;
    }
}