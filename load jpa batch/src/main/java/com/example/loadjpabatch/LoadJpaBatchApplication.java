package com.example.loadjpabatch;

import com.example.loadjpabatch.services.LoadDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@RequiredArgsConstructor
@SpringBootApplication
public class LoadJpaBatchApplication  implements CommandLineRunner {
	private final LoadDataBase loadDataBase;

	public static void main(String[] args) {
		SpringApplication.run(LoadJpaBatchApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		String path = "/home/luigi/Descargas/data.csv";
		String delimiter = ",";
		Long initialTime = System.currentTimeMillis();
		loadDataBase.loadData(path,delimiter);
		Long endTime = System.currentTimeMillis();

		System.out.println("Segundos: "+(endTime - initialTime)/1000);
	}
}
