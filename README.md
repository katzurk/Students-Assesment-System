# Temat: JustGrades - system oceniania studentów
## prowadzący: Wojciech Sitek
Projekt jest ćwiczeniem zespołowym mającym na celu głównie nabycie umiejętności projektowania baz danych. Rezultatem projektu ma być działający system złożony z bazy danych wraz z prostą aplikacją wykorzystującą tę bazę zarówno do działań operacyjnych jak i analityczno - raportowych.

Zespół nr. 2
## Skład zespołu:
* Diana Pelin (dalej jako D)
* Alesia Filinkova (dalej jako A)
* Katarzyna Kanicka (dalej jako K)
* Weronika Maślana (dalej jako W)

## Czas na wykonanie projektu
Początek: 	 10.03.2024
Koniec: 	 23.06.2024

## Realizacja projektu:
* baza danych typu relacyjnego: Oracle
* kod aplikacji napisany w języku java z wykorzystaniem framework'u Spring oraz biblioteki Hibernate
* frontend: React

## Funkcjonalności: 
ilość = 4os * 3 = 12

1. rejestracja i logowanie użytkownika
* role i uprawnienia:
* * student:
 2. zapisywanie się na przedmioty
 3. przegląd ocen końcowych
 4. przegląd zdobytych punktów z przedmiotu
 5. odwołanie się do oceny
* * wykładowca:
 6. wystawienie ocen
 7. przegląd profilu studenta z danego przedmiotu (zbiór punktów z przedmiotu dla danego studenta zapisanego na przedmiot)
 8. utworzenie schematu przedmiotu (np. ilość labów i za ile punktów, czy jest egzamin i za ile punktów)
 9. utworzenie rejestracji na przedmiot
 10. automatyczne wyliczenie oceny końcowej
* * wszyscy:
11. wyświetlanie wykresów (np. przekrój ocen z kolokwium, rozkład ocen końcowych wśród studentów z danego przedmiotu)
12. harmonogram zajęć
13. *(dodatkowe) ankiety na temat danego przedmiotu



## Etapy projektu 
Projekt jest realizowany w 3 zasadniczych etapach:

1. zdefiniowanie modelu pojęciowego,
2. zdefiniowanie logicznego modelu danych dla bazy relacyjnej oraz projekt aplikacji, przy czym część logiki aplikacyjnej powinna być zrealizowana w formie procedur składowanych w bazie danych;
3. stworzenie modelu fizycznego, implementacja bazy danych i aplikacji, zapewnienie danych testowych, testy.

## Wymagania projektu
W ramach projektu wymagane jest wykonanie następujących zadań i produktów:

1. opracowanie modelu pojęciowego (E-R),
2. na podstawie modelu pojęciowego opracowanie relacyjnego logicznego modelu danych,
3. zaprojektowanie funkcjonalności aplikacji: części operacyjnej (transakcyjnej) oraz części analityczno-raportowej,
4. optymalizacja modelu logicznego (w szczególności denormalizacja) w celu maksymalizacji wydajności systemu,
5. opracowanie elementów funkcjonalnych na poziomie bazy danych (triggery, procedury składowane),
6. dobór technologii bazodanowej, instalacja i konfiguracja środowiska,
7. dobór technologii realizacji aplikacji, instalacja i konfiguracja środowiska rozwojowego,
8. opracowanie, wdrożenie i optymalizacja modelu fizycznego,
9. opracowanie scenariuszy i danych testowych,
10. opracowanie dokumentacji analityczno-projektowej (w szczególności diagramów modeli danych z opisami),
11. opracowanie dokumentacji użytkowej aplikacji.

## Ocena projektu
Ocenie podlegają głównie:

1. poprawność i jakość modeli,
2. dobór technologii realizacji projektu,
3. funkcjonalność i jakość aplikacji,
4. efektywność przyjętych rozwiązań,
5. jakość dokumentacji,
6. terminowość realizacji zadań.

## Informacje
* Projekty realizowane są w zespołach 5-osobowych. W wyjątkowych wypadkach skład zespołu może różnić się o 1 osobę od tej normy.

* Treść zadania, harmonogram etapów oraz wszelkie inne szczegółowe aspekty dotyczące projektu są uzgadniane z prowadzącym.

* Nie ma żadnych preferowanych technologii dotyczących realizacji projektu - ani w warstwie bazodanowej, ani aplikacyjnej. Dobór rozwiązań technicznych, ich instalacja i konfiguracja są elementami prac projektowych i leżą w gestii zespołu projektowego.

* Do tworzenia modeli danych zalecane jest wykorzystanie narzędzia Oracle SQL Developer Data Modeler.

* Do komunikacji w ramach zespołów projektowych służą prywatne kanały zdefiniowane dla każdego zespołu na uczelnianej platformie Microsoft Teams. Na Microsoft Teams zostały również opublikowane zadania przypisane do poszczególnych zespołów. Jest to miejsce, w którym zespoły umieszczają wszelkie artefakty (dokumentację, kod, etc.) związane z realizacją projektu.