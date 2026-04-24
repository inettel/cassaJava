1. В структуре проекта указать Java 8 с поддержкой FX, как правило нужна версия JAVA сборки FULL:
1.1. File (бургер меню сверху слева) -> Project Structure -> Project -> SDK
1.2. SDK: Liberica-1.8. BellSoft Liberica 1.8.0_482
1.3. Language level: 8
2. Указать переменную окружения $env:JAVA_HOME = "C:\Users\User\Downloads\bellsoft-jdk8u482+10-windows-amd64-full\jdk8u482-full" - должна ссылаться на нужную версию Java с поддержкой FX(Full вариант). Команда должна выполняться в КОРНЕ проекта;
3. Сборка JAR файла выполняется в КОРНЕ проекта: mvn clean package
4. Запускаемый jar файл должен находиться в той же папке что и utm5_payment_tool
5  Должен существовать путь:  C:\Program files\NetUP\UTM5\utm5_payment_tool.cfg
6. Команда запуска в CMD не в PowerShell: C:\Users\User\IdeaProjects\cassaJava\target>"C:\Java\jdk8u482-full\bin\java.exe" -Dfile.encoding=UTF-8 -jar cassa-3.0.jar
