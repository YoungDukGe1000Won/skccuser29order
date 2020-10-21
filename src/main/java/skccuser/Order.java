package skccuser;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String productId;
    private Integer qty;
    private String status;

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        skccuser.external.Payment payment = new skccuser.external.Payment();
        // mappings goes here
        payment.setOrderId(this.getId()) ;
        payment.setProductId(this.getProductId());
        payment.setQty(this.getQty());
        payment.setStatus("order");
        OrderApplication.applicationContext.getBean(skccuser.external.PaymentService.class)
            .pay(payment);


    }

    @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        BeanUtils.copyProperties(this, orderCancelled);
        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        skccuser.external.PaymentCancellation paymentCancellation = new skccuser.external.PaymentCancellation();
        // mappings goes here
        paymentCancellation.setOrderId(this.getId());
        paymentCancellation.setProductId(this.getProductId());
        paymentCancellation.setQty(this.getQty());
        paymentCancellation.setStatus("orderCancel");
        OrderApplication.applicationContext.getBean(skccuser.external.PaymentCancellationService.class)
            .paycancel(paymentCancellation);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
