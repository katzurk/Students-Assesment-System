# JustGrades - system oceniania studentów

- [English](README.md)

---

## Autorzy:
* Diana Pelin
* Alesia Filinkova
* Katarzyna Kanicka
* Weronika Maślana

## Instalacja środowiska
Zainstaluj NVM:

```curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash```

Node.js 22:

```nvm install 22 && nvm use 22```

pnpm:

```npm install -g pnpm@latest-10```

Zależności:

```pnpm install```

---

## Uruchomienie aplikacji

**Just-grades-rest (Spring)**:

```clear && ./mvnw spring-boot:run```

W przeglądarce wchodzimy na stronę:

```localhost:8080```

**Just-grades-ui (React)**:

```pnpm dev```

w przeglądarce wchodzimy na stronę:

```localhost:3000```

Dokumentacja swagger: 

```http://localhost:8080/swagger-ui/index.html#/```   

---

## Realizacja projektu:
* Baza danych typu relacyjnego: Oracle
* Kod aplikacji napisany w języku java z wykorzystaniem framework'u Spring oraz biblioteki Hibernate
* Frontend: Next.js, React - Typescript

---

## Funkcjonalności

### 1. Logowanie użytkownika

---

### Role i Uprawnienia

#### Student
3. Przegląd ocen końcowych  
4. Przegląd zdobytych punktów z przedmiotu  
5. Zapisywanie się na przedmioty  
6. Wypisywanie się z przedmiotów  

#### Wykładowca
7. Wystawienie ocen  
8. Przegląd profilu studenta z danego przedmiotu  
   - zbiór punktów z przedmiotu dla danego studenta zapisanego na przedmiot  
9. Utworzenie schematu przedmiotu  
   - np. ilość labów i za ile punktów, czy jest egzamin i za ile punktów, ile punktów trzeba zdobyć by zaliczyć  
10. Zamknięcie przedmiotu i automatyczne wyliczenie oceny końcowej  
11. Otwarcie i zamknięcie semestru  
12. Otwarcie i zamknięcie rejestracji  

#### Oboje
13. Wyświetlanie raportów  
   - np. przekrój ocen z kolokwium, rozkład ocen końcowych wśród studentów z danego przedmiotu  


---

## Etapy projektu
1. Zdefiniowanie modelu pojęciowego,
2. Zdefiniowanie logicznego modelu danych dla bazy relacyjnej oraz projekt aplikacji, przy czym część logiki aplikacyjnej powinna być zrealizowana w formie procedur składowanych w bazie danych,
3. Stworzenie modelu fizycznego, implementacja bazy danych i aplikacji, zapewnienie danych testowych, testy.

