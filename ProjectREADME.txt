1. В структуре проекта указать Java 8 с поддержкой FX, как правило нужна версия JAVA сборки FULL
2. Указать переменную окружения $env:JAVA_HOME = "C:\Users\User\Downloads\bellsoft-jdk8u482+10-windows-amd64-full\jdk8u482-full" - должна ссылаться на нужную версию Java с поддержкой FX(Full вариант). Команда должна выполняться в КОРНЕ проекта;
3. Сборка JAR файла выполняется в КОРНЕ проекта: mvn clean package
4. Команда запуска в CMD не в PowerShell: C:\Users\User\IdeaProjects\cassaJava\target>"C:\Java\jdk8u482-full\bin\java.exe" -Dfile.encoding=UTF-8 -jar cassa-3.0.jar