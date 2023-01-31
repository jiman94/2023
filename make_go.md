
#### Makefile 

```sh
# executable name
BINARY=main

# build all the things
all: build

# build the binary
build:
	go build -o $(BINARY)

# run the binary
run: build
	./$(BINARY)

# clean up
clean:
	go clean
	rm -f $(BINARY)

# run tests
test:
	go test ./...
```

