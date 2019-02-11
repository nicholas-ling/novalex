## How to run
1.docker pull jamesdbloom/docker-java8-maven  
2.docker run -it jamesdbloom/docker-java8-maven  
3.ssh-keygen -t rsa -b 4096 -C "your_email@example.com"  
4.change to "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts"  
5.mvn clean install exec:java -Dexec.mainClass="com.novalex.httpcomponent.Main"  
