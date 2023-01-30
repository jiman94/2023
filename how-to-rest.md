
```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(CloseableHttpClient httpClient) {
    return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
  }

  @Bean
  public CloseableHttpClient httpClient() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(100);
    connectionManager.setDefaultMaxPerRoute(20);
    return HttpClientBuilder.create()
      .setConnectionManager(connectionManager)
      .setDefaultRequestConfig(RequestConfig.custom().setReadTimeout(5000).build())
      .build();
  }

}

```


```java

import org.springframework.web.client.RestTemplate;

@Autowired
private RestTemplate restTemplate;



List<String> urls = Arrays.asList("http://example.com/api/1", "http://example.com/api/2", "http://example.com/api/3");
for (String url : urls) {
  ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
  String responseBody = response.getBody();
  // do something with the response
}



	  List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
 
		MappingJackson2HttpMessageConverter map = new MappingJackson2HttpMessageConverter();
		MappingJackson2XmlHttpMessageConverter xml = new MappingJackson2XmlHttpMessageConverter();
		messageConverters.add(map);
		messageConverters.add(xml);
		restTemplate.setMessageConverters(messageConverters);
```    
