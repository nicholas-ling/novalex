
1.docker pull jamesdbloom/docker-java8-maven
1.docker run -it jamesdbloom/docker-java8-maven
1.ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
1.change to "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts"
1.mvn clean install exec:java -Dexec.mainClass="com.novalex.httpcomponent.Main"
