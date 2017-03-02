cd ./csa-conference
../docker_image.sh
echo "csa-conference done."

cd ../csa-conference-tweets
../docker_image.sh
echo "csa-conference-tweets done."

cd ../csa-twitter-search
../docker_image.sh
echo "csa-twitter-search done."
