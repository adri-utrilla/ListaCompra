package edu.uclm.esi.flistacompra;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
 
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class AppListaCompraBE extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(AppListaCompraBE.class, args);
	}
 
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AppListaCompraBE.class);
	}
}