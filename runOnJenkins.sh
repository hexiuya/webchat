docker stop webchat
docker rm webchat
docker image rm webchat:0.0.1
docker build . -t webchat:0.0.1
docker run -it -d -p 8883:8883 --name webchat --network crm-network --network-alias alias-webchat webchat:0.0.1
