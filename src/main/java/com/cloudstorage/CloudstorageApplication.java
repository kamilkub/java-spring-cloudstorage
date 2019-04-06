package com.cloudstorage;

import com.cloudstorage.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;

import javax.annotation.Resource;

@SpringBootApplication
public class CloudstorageApplication {


	public static void main(String[] args) {
		SpringApplication.run(CloudstorageApplication.class, args);
	}



}
