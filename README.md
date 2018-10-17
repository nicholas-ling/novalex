docker pull jamesdbloom/docker-java8-maven
docker run -it jamesdbloom/docker-java8-maven
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
change to "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts"
mvn clean install exec:java -Dexec.mainClass="com.novalex.httpcomponent.Main"
