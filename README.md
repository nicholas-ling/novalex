docker pull jamesdbloom/docker-java8-maven
docker run -it jamesdbloom/docker-java8-maven
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
mvn clean install
mvn exec:java -Dexec.mainClass="com.novalex.httpcomponent.Main"
