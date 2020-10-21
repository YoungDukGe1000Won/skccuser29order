package skccuser;

import skccuser.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_Updatestatus(@Payload Shipped shipped){

        if(shipped.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(shipped.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(shipped.getStatus());

            orderRepository.save(order);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCanceled_Updatestatus(@Payload DeliveryCanceled deliveryCanceled){

        if(deliveryCanceled.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(deliveryCanceled.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(deliveryCanceled.getStatus());

            orderRepository.save(order);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOffered_Updatestatus(@Payload Offered offered){

        if(offered.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(offered.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(offered.getStatus());

            orderRepository.save(order);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCouponCanceled_Updatestatus(@Payload CouponCanceled couponCanceled){

        if(couponCanceled.isMe()){
            Optional<Order> orderOptional = orderRepository.findById(couponCanceled.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(couponCanceled.getStatus());

            orderRepository.save(order);
        }
    }

}
