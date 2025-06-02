# Temat: JustGrades - system oceniania studentów
## prowadzący: Wojciech Sitek
Projekt jest ćwiczeniem zespołowym mającym na celu głównie nabycie umiejętności projektowania baz danych. Rezultatem projektu ma być działający system złożony z bazy danych wraz z prostą aplikacją wykorzystującą tę bazę zarówno do działań operacyjnych jak i analityczno - raportowych.

## Skład zespołu:
* Diana Pelin
* Alesia Filinkova
* Katarzyna Kanicka
* Weronika Maślana

## Instalacja środowiska
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash

install node.js

nvm install 22

npm install -g pnpm@latest-10

nvm use 22

npm install @mui/material @emotion/react @emotion/styled

npm install chart.js react-chartjs-2

## Uruchomienie aplikacji
* just-grades-rest (spring):

 clear && ./mvnw spring-boot:run

ewentualnie w przeglądarce wchodzimy na stronę:

 localhost:8080

* just-grades-ui (react):

 pnpm dev

w przeglądarce wchodzimy na stronę:

 localhost:3000

dokumentacja swagger: http://localhost:8080/swagger-ui/index.html#/   

## Realizacja projektu:
* Baza danych typu relacyjnego: Oracle
* Kod aplikacji napisany w języku java z wykorzystaniem framework'u Spring oraz biblioteki Hibernate
* Frontend: React

## Funkcjonalności:
1. Logowanie użytkownika
**role i uprawnienia:**
*Student:*
2. Przegląd ocen końcowych,
3. Przegląd zdobytych punktów z przedmiotu,
4. Zapisywanie się na przedmioty
5. Wypisywanie się z przedmiotów
*Wykładowca:*
6. Wystawienie ocen,
7. Przegląd profilu studenta z danego przedmiotu (zbiór punktów z przedmiotu dla danego studenta zapisanego na przedmiot)
8. Utworzenie schematu przedmiotu (np. ilość labów i za ile punktów, czy jest egzamin i za ile punktów, ile punktów trzeba zdobyć by zaliczyć),
9. Zamknięcie przedmiotu i automatyczne wyliczenie oceny końcowej,
10. Otwarcie i zamknięcie semestru
11. Otwarcie i zamknięcie rejestracji
*Oboje:*
12. Wyświetlanie raportów (np. przekrój ocen z kolokwium, rozkład ocen końcowych wśród studentów z danego przedmiotu)


## Etapy projektu
1. Zdefiniowanie modelu pojęciowego,
2. Zdefiniowanie logicznego modelu danych dla bazy relacyjnej oraz projekt aplikacji, przy czym część logiki aplikacyjnej powinna być zrealizowana w formie procedur składowanych w bazie danych,
3. Stworzenie modelu fizycznego, implementacja bazy danych i aplikacji, zapewnienie danych testowych, testy.

