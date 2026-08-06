.PHONY: test test-smoke run

test:
	mvn clean test

test-smoke:
	mvn clean compile exec:java -Dexec.mainClass="com.moneywise.SmokeTest"

run:
	mvn clean compile exec:java -Dexec.mainClass="com.moneywise.Main"