package com.example.BookStore;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.BookStore.Core.DataUtil;
import com.example.BookStore.Entity.*;
import com.mongodb.internal.operation.DropCollectionOperation;

/**
 * Central application class containing both general application and web configuration as well as a main-method to
 * bootstrap the application using Spring Boot.
 *
 * @see #main(String[])
 * @see SpringApplication
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class BookStoreApplication 
{			
		/**
	 * Bootstraps the application in standalone mode (i.e. java -jar).
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}

	private LoadInfo loadConfig = null;
	/**
	 * List Book in store
	 *
	 * @param args
	 */
	private List<Book> getBooks(MongoOperations mongo) {
		String collectionName = mongo.getCollectionName(LoadInfo.class);
		if (collectionName == null) {
			mongo.createCollection(LoadInfo.class);
			collectionName = mongo.getCollectionName(LoadInfo.class);
		}
		var configList = mongo.findAll(LoadInfo.class);
		if (configList.size() == 0)
		{
			Properties yaml = loadBooksYaml();
			MapConfigurationPropertySource source = new MapConfigurationPropertySource(yaml);
			var loadbookConfigList =  new Binder(source).bind("loadinfo", Bindable.listOf(LoadInfo.class)).get();
			loadConfig = loadbookConfigList.get(0);
			
			String recommendedBookUrl = loadConfig.getRecommendBookURI();
			String allBookUrl = loadConfig.getAllBookURI();			
			if (recommendedBookUrl == null) loadConfig.setRecommendBookURI(DataUtil.DefaultRecommendedBookUrl);
			if (allBookUrl == null) loadConfig.setAllBookURI(DataUtil.DefaultAllBookUrl);
			
			mongo.save(loadConfig);
		}
		else 
		{
			loadConfig = configList.get(0);
		}
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
        formatter.localizedBy(Locale.ENGLISH);
        LocalDate lastUpdate = LocalDate.parse(loadConfig.getLastUpdate(), formatter);
                
        if (now.compareTo(lastUpdate) > 0) {
        	try 
        	{
        		var allBooks = DataUtil.GetAllBook(loadConfig);
        		String lastUpdateStr = now.format(formatter);
        		loadConfig.setLastUpdate(lastUpdateStr);
        		mongo.save(loadConfig);
        		return allBooks;
        	}
        	catch(Throwable th) {
        		return null;        		
        	}
        }
        
        return null;
    }
	
	@Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {              
            var books = getBooks(mongo);
            if (books != null) {
            	mongo.dropCollection(Book.class);
            	mongo.createCollection(Book.class);
            	for(int i=0;i<books.size();i++) {
            		mongo.save(books.get(i));
            	}
            }            
        };
    }
	
	private Properties loadBooksYaml() {
        YamlPropertiesFactoryBean properties = new YamlPropertiesFactoryBean();
        properties.setResources(new ClassPathResource("bookConfig.yml"));
        return properties.getObject();
    }
}
