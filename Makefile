build:
	docker build -t fsp/parse-rate .
run: build
	docker run -p 10100:8080 fsp/parse-rate
