# back
# устанавливаем самую лёгкую версию JVM
FROM openjdk:11

# указываем ярлык. Например, разработчика образа и проч. Необязательный пункт.
LABEL maintainer="sadopwnz@yandex.ru"

# указываем точку монтирования для внешних данных внутри контейнера (как мы помним, это Линукс)
VOLUME /tmp

# внешний порт, по которому наше приложение будет доступно извне
EXPOSE 8090

# указываем, где в нашем приложении лежит джарник
ARG JAR_FILE=target/miracle-0.0.1-SNAPSHOT.jar

# добавляем джарник в образ под именем miracle-backend.jar
ADD ${JAR_FILE} miracle-backend.jar

# команда запуска джарника
ENTRYPOINT ["java","-jar","/miracle-backend.jar"]