ng build --prod
cp -r ./dist ./docker/www/
sudo docker build -t csa/csa-spa ./docker