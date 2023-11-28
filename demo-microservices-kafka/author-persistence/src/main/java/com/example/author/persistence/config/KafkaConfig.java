package com.example.author.persistence.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic booksPublishedTopic(final KafkaConfigProps kafkaConfigProps) { // tạo topic
        // TopicBuilder.name(kafkaConfigProps.getTopic()) tạo topic với tên là book-published
        // partitions(10) tạo 10 partition
        // replicas(1) tạo 1 replica

        // partition là một phần của topic, mỗi partition có thể có nhiều consumer
        // replica là một bản sao của partition, mỗi partition có thể có nhiều replica
        // mỗi partition chỉ có thể có một leader, các replica khác là follower
        // leader sẽ nhận tất cả các request của producer và consumer, follower sẽ đồng bộ dữ liệu với leader
        // khi leader bị lỗi, follower sẽ được chọn làm leader
        // khi một consumer subscribe vào một topic, nó sẽ nhận message từ tất cả các partition của topic đó
        // khi một producer gửi message tới một topic, message sẽ được gửi tới leader của một partition
        // sau đó leader sẽ gửi message tới tất cả các follower của partition đó
        return TopicBuilder.name(kafkaConfigProps.getTopic())
                .partitions(10)
                .replicas(1)
                .build();
    }
}
