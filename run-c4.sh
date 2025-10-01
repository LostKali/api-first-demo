docker rm -f api-first-c4 || true && \
docker run -d \
  --name api-first-c4 \
  -p 8778:8080 \
  -v ./docs/arch/c4:/usr/local/structurizr \
  structurizr/lite