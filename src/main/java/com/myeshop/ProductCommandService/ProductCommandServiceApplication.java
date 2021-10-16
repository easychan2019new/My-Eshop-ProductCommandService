package com.myeshop.ProductCommandService;

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductCommandServiceApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCommandServiceApplication.class, args);
	}


	// Create snapshot
	@Bean(name = "productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 500);
	}
}
