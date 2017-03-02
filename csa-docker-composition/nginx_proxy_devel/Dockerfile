FROM nginx:latest
ADD csa-conf /etc/nginx/sites-available/csa-conf
RUN sh -c 'mkdir /etc/nginx/sites-enabled && ln -s ../sites-available/csa-conf /etc/nginx/sites-enabled/csa-conf'
ADD nginx.conf /etc/nginx/nginx.conf
