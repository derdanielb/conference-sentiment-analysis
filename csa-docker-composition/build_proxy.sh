sudo docker build -t csa/nginx_proxy_devel ./nginx_proxy_devel
sudo docker run -d --network=host --name csa_nginx_proxy_devel csa/nginx_proxy_devel:latest