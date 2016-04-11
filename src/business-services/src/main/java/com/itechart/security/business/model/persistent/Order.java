package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.ObjectTypes;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Order
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "`order`")
public class Order extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product", nullable = false, length = 100)
    private String product;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String getObjectType() {
        return ObjectTypes.ORDER.getName();
    }
}
