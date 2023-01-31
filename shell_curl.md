# Here is a sample code for a curl program in bash shell:

```
#!/bin/bash

# Make a GET request to retrieve the contents of a URL
curl -X GET http://www.example.com

# Make a POST request to submit data to a URL
curl -X POST -d "param1=value1&param2=value2" http://www.example.com

# Make a request with a custom header
curl -H "Authorization: Bearer my_token" http://www.example.com

# Make a request to retrieve the contents of a URL and save the result to a file
curl http://www.example.com > example.html

# Make a request with SSL/TLS encryption
curl --insecure https://www.example.com
```
